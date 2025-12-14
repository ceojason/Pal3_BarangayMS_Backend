package com.javaguides.sps.model.returnmodel;

import com.javaguides.sps.model.SubjectsModel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class InitializeSubjectModel {

    List<SubjectsModel> grade11SubjectList =  new ArrayList<>();
    List<SubjectsModel> grade12SubjectList = new ArrayList<>();

}
