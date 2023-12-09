package com.lorbeer.refugeeapp.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lorbeer.refugeeapp.domain.Contestants;
import com.lorbeer.refugeeapp.repository.ContestantsRepository;

import java.util.List;

public class ContestantsViewModel extends ViewModel {

    ContestantsRepository contestantsRepository;

    private MutableLiveData<List<Contestants>> allContestants;
    private MutableLiveData<List<Contestants>> contestantsForCourse;
    private MutableLiveData<Contestants> contestantsByMail;




    public void queryAllContestants(Context context) {
        contestantsRepository = ContestantsRepository.getINSTANCE();
        allContestants = contestantsRepository.getAllContestants(context);
    }

    public LiveData<List<Contestants>> getAllContestants() {
        return allContestants;
    }

    public void queryContestantByMail(String email, Context context) {
        contestantsRepository = ContestantsRepository.getINSTANCE();
        contestantsByMail = contestantsRepository.getContestantbyMail(email,context);
    }

    public LiveData<Contestants> getContestantByMail() {
        return contestantsByMail;
    }

    public void queryContestantsForCourses(Integer id, Context context) {
        contestantsRepository = ContestantsRepository.getINSTANCE();
        contestantsForCourse = contestantsRepository.getContestantsForCourse(id, context);
    }

    public LiveData<List<Contestants>> getContestantsForCourses() {
        return contestantsForCourse;
    }
}
