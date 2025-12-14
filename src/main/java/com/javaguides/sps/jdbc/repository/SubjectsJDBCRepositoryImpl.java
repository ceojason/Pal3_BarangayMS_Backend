package com.javaguides.sps.jdbc.repository;

import com.javaguides.sps.enums.YearlevelEnum;
import com.javaguides.sps.jdbc.repository.basejdbcrepository.BaseJDBCRepositoryImpl;
import com.javaguides.sps.model.EnrolledSubjectsModel;
import com.javaguides.sps.model.SubjectsModel;
import com.javaguides.sps.helper.DbTableUtil;
import com.javaguides.sps.helper.GenericModelRowMapper;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SubjectsJDBCRepositoryImpl extends BaseJDBCRepositoryImpl implements  SubjectsJDBCRepository {

    private static final String tblSubjects = DbTableUtil.getTableName(SubjectsModel.class);
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    static final String tblEnrolledSubjectsG11 = "tbl_enrolledsubjects_g11";
    static final String tblEnrolledSubjectsG12 = "tbl_enrolledsubjects_g12";

    public SubjectsJDBCRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate);
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<SubjectsModel> getAllSubjectsByKey(String yearlevelKey, String strandKey) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        List<String> yearlevelKeyList = new ArrayList<>();
        if (yearlevelKey!=null && yearlevelKey.equals(YearlevelEnum.GRADE11.getKeyStr())) {
            yearlevelKeyList.add(YearlevelEnum.GRADE11.getKeyStr());
            yearlevelKeyList.add(YearlevelEnum.GRADE12.getKeyStr());
        }else{
            yearlevelKeyList.add(YearlevelEnum.GRADE12.getKeyStr());
        }

        map.addValue("yearlevelKeyList", yearlevelKeyList);
        map.addValue("strandKey", strandKey);

        StringBuilder sql = new StringBuilder()
                .append(" SELECT * FROM ").append(tblSubjects)
                .append(" WHERE ").append(" STRAND_KEY = :strandKey AND YEARLEVEL_KEY IN (:yearlevelKeyList) ")
                .append(" ORDER BY SEMESTER ASC, YEARLEVEL_KEY ASC ")
                ;
        return namedParameterJdbcTemplate.query(sql.toString(), map, new BeanPropertyRowMapper<>(SubjectsModel.class));
    }

    @Override
    public List<EnrolledSubjectsModel> findEnrolledSubjectsByLrn(String lrn, boolean isGrade11) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        String tblOpt = isGrade11 ? tblEnrolledSubjectsG11 : tblEnrolledSubjectsG12;
        map.addValue("lrn", lrn);

        StringBuilder sql = new StringBuilder()
                .append(" SELECT * ").append(" FROM ").append(tblOpt).append(" WHERE ").append(" lrn = :lrn" );
        return namedParameterJdbcTemplate.query(sql.toString(), map, new GenericModelRowMapper<>(EnrolledSubjectsModel.class));
    }

    @Override
    public int saveSubjects(boolean isGrade11, EnrolledSubjectsModel enrolledSubjectsModel) {
        if (isGrade11) {
            return save(enrolledSubjectsModel, tblEnrolledSubjectsG11);
        }else{
            return save(enrolledSubjectsModel, tblEnrolledSubjectsG12);
        }
    }

    @Override
    public int deleteByLrn(String lrn, boolean isGrade11) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("lrn", lrn);
        String tblOpt = isGrade11 ? tblEnrolledSubjectsG11 : tblEnrolledSubjectsG12;

        StringBuilder sql = new StringBuilder()
                .append(" DELETE FROM ").append(tblOpt).append(" WHERE ").append(" LRN = :lrn ");
        return namedParameterJdbcTemplate.update(sql.toString(), map);
    }

    @Override
    public int deleteByIdList(List<String> idList, boolean isGrade11) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("idList", idList);
        String tblOpt = isGrade11 ? tblEnrolledSubjectsG11 : tblEnrolledSubjectsG12;

        StringBuilder sql = new StringBuilder()
                .append(" DELETE FROM ").append(tblOpt).append(" WHERE ").append(" ID IN (:idList) ");
        return namedParameterJdbcTemplate.update(sql.toString(), map);
    }

}
