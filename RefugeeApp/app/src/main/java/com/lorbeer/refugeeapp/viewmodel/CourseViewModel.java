package com.lorbeer.refugeeapp.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lorbeer.refugeeapp.domain.Contestants;
import com.lorbeer.refugeeapp.domain.Course;
import com.lorbeer.refugeeapp.repository.CourseRepository;

import java.util.List;

public class CourseViewModel extends ViewModel {

    private MutableLiveData<List<Course>> allCourses;

    private MutableLiveData<Boolean> createCourse;

    private MutableLiveData<List<Contestants>> contestantsForCourse;
    private CourseRepository courseRepository;

    public void queryAllCourses() {
        courseRepository = CourseRepository.getINSTANCE();
        allCourses = courseRepository.getAllCourses();
    }

    public LiveData<List<Course>> getAllCourses() {
        return allCourses;
    }

    public void createCourse(Course course) {
        courseRepository = CourseRepository.getINSTANCE();
        createCourse = courseRepository.createCourse(course);
    }

    public LiveData<Boolean> courseCreated() {
        return createCourse;
    }

    public void getContestantsForCourse(Integer id) {
        courseRepository = CourseRepository.getINSTANCE();
        contestantsForCourse = courseRepository.getContestantsForCourse(id);
    }

    public LiveData<List<Contestants>> getContestantsForCourses() {
        return contestantsForCourse;
    }




}
