package com.lorbeer.refugeeuserapp.viewmodel;

import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lorbeer.refugeeuserapp.domain.Contestants;
import com.lorbeer.refugeeuserapp.domain.Course;
import com.lorbeer.refugeeuserapp.repository.CourseRepository;

import java.util.List;
import java.util.Map;

public class CourseViewModel extends ViewModel {

    private MutableLiveData<List<Course>> allCourses;
    private CourseRepository courseRepository;
    private MutableLiveData<Course> courseByName;

    private MutableLiveData<Boolean> enrollContestant;

    private MutableLiveData<Map<String, Double>> geocode;


    public void geocodeAddress(String address) {
        courseRepository = CourseRepository.getINSTANCE();
        geocode = courseRepository.getAddress(address);
    }

    public LiveData<Map<String, Double>> geocoded() {
        return geocode;
    }

    public void enrollContestant(Integer course_id, Contestants contestants) {
        courseRepository = CourseRepository.getINSTANCE();
        enrollContestant = courseRepository.entrollContestant(course_id, contestants);
    }

    public LiveData<Boolean> contestantEnrolled() {
        return enrollContestant;
    }

    public void queryAllCourses(Context context) {
        courseRepository = CourseRepository.getINSTANCE();
        allCourses = courseRepository.getAllCourses(context);
    }

    public LiveData<List<Course>> getAllCourses() {
        return allCourses;
    }

    public void getCourseByName(String name,Context context) {
        courseRepository = CourseRepository.getINSTANCE();
        courseByName = courseRepository.getCourseByName(name, context);
    }

    public LiveData<Course> getCourseByName() {
        return courseByName;
    }
}
