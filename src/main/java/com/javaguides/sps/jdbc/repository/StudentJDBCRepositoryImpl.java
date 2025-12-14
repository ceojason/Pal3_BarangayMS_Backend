package com.javaguides.sps.jdbc.repository;

import com.javaguides.sps.enums.SystemStatusEnum;
import com.javaguides.sps.jdbc.repository.basejdbcrepository.BaseJDBCRepositoryImpl;
import com.javaguides.sps.model.SectionModel;
import com.javaguides.sps.model.StudentModel;
import com.javaguides.sps.helper.DbTableUtil;
import com.javaguides.sps.helper.GenericModelRowMapper;
import com.javaguides.sps.helper.GenericRowMapper;
import com.javaguides.sps.model.requestmodel.searchrequest.StudentSearchRequest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class StudentJDBCRepositoryImpl extends BaseJDBCRepositoryImpl implements StudentJDBCRepository {

    private static final String tblStudent = DbTableUtil.getTableName(StudentModel.class);
    private static final String tblSection = DbTableUtil.getTableName(SectionModel.class);
    static final String tblStudentAlias = DbTableUtil.getTableAlias(StudentModel.class);
    static final String tblSectionAlias = DbTableUtil.getTableAlias(SectionModel.class);
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public StudentJDBCRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate);
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public StudentModel findById(String id) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", id);
        map.addValue("statusList", List.of(SystemStatusEnum.GRADUATE, SystemStatusEnum.UNDER_GRADUATE));

        StringBuilder sql = new StringBuilder()
                .append(" SELECT * ")
                .append(" FROM ")
                .append(tblStudent)
                .append(" WHERE ")
                .append(" id = :id")
                .append(" AND status IN (:statusList)");
        return namedParameterJdbcTemplate.queryForObject(sql.toString(), map, new BeanPropertyRowMapper<>(StudentModel.class));
    }

    @Override
    public StudentModel findByLrn(String lrn) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("lrn", lrn);

        StringBuilder sql = new StringBuilder()
                .append(" SELECT * ").append(" FROM ").append(tblStudent).append(" WHERE ").append(" lrn = :lrn");
        return namedParameterJdbcTemplate.queryForObject(sql.toString(), map, new GenericModelRowMapper<>(StudentModel.class));
    }

    @Override
    public Long countStudentByLrn(String lrn) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("lrn", lrn);

        StringBuilder sql = new StringBuilder()
                .append(" SELECT COUNT(*) as count ")
                .append(" FROM ")
                .append(tblStudent)
                .append(" WHERE ")
                .append(" LRN = :lrn ");
        return namedParameterJdbcTemplate.queryForObject(sql.toString(), map, Long.class);
    }

    @Override
    public int saveEnrollment(StudentModel modelObj) {
        return save(modelObj);
    }

    @Override
    public int deleteByLrn(String lrn) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("lrn", lrn);

        StringBuilder sql = new StringBuilder()
                .append(" DELETE FROM ").append(tblStudent).append(" WHERE ").append(" LRN = :lrn ");
        return namedParameterJdbcTemplate.update(sql.toString(), map);
    }

    @Override
    public Page<StudentModel> searchStudent(StudentSearchRequest requestObj, PageRequest page) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        List<StudentModel> list = new ArrayList<>();
        StringBuilder whereClause = createWhereClause(requestObj, map);
        whereClause.append(getOrderBy(page, StudentModel.class));

        Integer count = namedParameterJdbcTemplate.queryForObject(countQry(whereClause), map, Integer.class);
        if (count!=null && count>0) {
            list = mapResultToStudentModel(namedParameterJdbcTemplate.query(selectQry(whereClause), map, new GenericRowMapper()));
        }else{
            count=0;
        }
        return new PageImpl<>(list, page, count);
    }

    public List<StudentModel> mapResultToStudentModel(List<Map<String, Object>> list) {
        List<StudentModel> studentModelList = new ArrayList<>();
        for (Map<String, Object> row : list) {
            StudentModel student = new StudentModel();
            for (Map.Entry<String, Object> entry : row.entrySet()) {
                String key = entry.getKey();
                if (key.startsWith("tst_")) {
                    String fieldName = key.substring(4);
                    try {
                        Field field = getField(StudentModel.class, fieldName);
                        field.setAccessible(true);
                        field.set(student, entry.getValue());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

                if (key.startsWith("tss_")) {
                    String fieldName = key.substring(4);
                    if (fieldName.equals("sectionNm")) {
                        student.setSectionNm((String) row.get(key));
                    }
                }
            }
            studentModelList.add(student);
        }
        return studentModelList;
    }

    private String countQry(StringBuilder whereClause) {
        StringBuilder sql = new StringBuilder()
                .append( " SELECT ").append(count()).append(" FROM ").append(DbTableUtil.getTableNameWithAlias(StudentModel.class)).append(whereClause);
        return sql.toString();
    }

    private String selectQry(StringBuilder whereClause) {
        StringBuilder sql = new StringBuilder()
                .append(" SELECT ").append(DbTableUtil.buildSelectClause(StudentModel.class)).append(", ")
                .append(DbTableUtil.buildSelectClause(SectionModel.class))
                .append(" FROM ").append(DbTableUtil.getTableNameWithAlias(StudentModel.class))
                .append(" LEFT JOIN ").append(DbTableUtil.getTableNameWithAlias(SectionModel.class))
                .append(" ON ")
                .append(tblStudentAlias).append(".SECTION_ID = ")
                .append(tblSectionAlias).append(".ID ")
                .append(whereClause);
        return sql.toString();
    }

    private StringBuilder createWhereClause(StudentSearchRequest request, MapSqlParameterSource map) {
        StringBuilder where = new StringBuilder();
        List<String> conditions = new ArrayList<>();

        if (request.getLrn() != null && !request.getLrn().isEmpty()) {
            map.addValue("lrn", request.getLrn().trim());
            conditions.add("LRN = :lrn");
        }

        if (request.getFirstNm() != null && !request.getFirstNm().isEmpty()) {
            map.addValue("firstNm", request.getFirstNm().trim().toUpperCase());
            conditions.add("FIRST_NM = :firstNm");
        }

        if (request.getLastNm() != null && !request.getLastNm().isEmpty()) {
            map.addValue("lastNm", request.getLastNm().trim().toUpperCase());
            conditions.add("LAST_NM = :lastNm");
        }

        if (request.getEmailAddress() != null && !request.getEmailAddress().isEmpty()) {
            map.addValue("emailAddress", request.getEmailAddress().trim());
            conditions.add("EMAIL_ADDRESS = :emailAddress");
        }

        if (!conditions.isEmpty()) {
            where.append(" WHERE ").append(String.join(" AND ", conditions));
        }

        return where;
    }

}
