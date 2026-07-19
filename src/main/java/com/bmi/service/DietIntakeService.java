package com.bmi.service;

import com.bmi.dao.DietRecordDAO;
import com.bmi.model.DietRecord;

import java.util.List;

/**
 * 膳食摄入服务层
 */
public class DietIntakeService {
    private final DietRecordDAO dietRecordDAO = new DietRecordDAO();

    /**
     * 保存膳食记录（同一天已存在则覆盖更新，否则新增）
     * @return [是否成功, 是否为更新, 错误信息(null 表示成功)]
     */
    public Object[] saveRecord(DietRecord r) {
        try {
            DietRecord exist = dietRecordDAO.findByDate(r.getUserId(), r.getRecordDate());
            if (exist != null) {
                r.setId(exist.getId());
                boolean ok = dietRecordDAO.update(r);
                return new Object[]{ok, true, ok ? null : "数据库更新失败"};
            }
            boolean ok = dietRecordDAO.add(r);
            return new Object[]{ok, false, ok ? null : "数据库插入失败"};
        } catch (Exception e) {
            return new Object[]{false, false, e.getMessage()};
        }
    }

    /**
     * 查询用户所有膳食记录
     */
    public List<DietRecord> findByUserId(int userId) {
        return dietRecordDAO.findByUserId(userId);
    }

    /**
     * 查询用户最近N天的记录
     */
    public List<DietRecord> findRecent(int userId, int days) {
        return dietRecordDAO.findRecentByUserId(userId, days);
    }

    /**
     * 查询某用户某天的记录 (无则返回 null)
     */
    public DietRecord findByDate(int userId, java.util.Date date) {
        return dietRecordDAO.findByDate(userId, date);
    }

    public boolean deleteRecord(int id) {
        return dietRecordDAO.delete(id);
    }

    /**
     * 计算平均摄入（对最近N天求平均）
     */
    public double[] calcAverages(List<DietRecord> records) {
        // 顺序: 碳水, 脂肪, 蛋白质, 热量, 膳食纤维, 胆固醇, 饮水量
        double[] sums = new double[7];
        if (records == null || records.isEmpty()) return sums;
        for (DietRecord r : records) {
            sums[0] += r.getCarbs();
            sums[1] += r.getFat();
            sums[2] += r.getProtein();
            sums[3] += r.getCalories();
            sums[4] += r.getFiber();
            sums[5] += r.getCholesterol();
            sums[6] += r.getWater();
        }
        for (int i = 0; i < sums.length; i++) sums[i] /= records.size();
        return sums;
    }
}
