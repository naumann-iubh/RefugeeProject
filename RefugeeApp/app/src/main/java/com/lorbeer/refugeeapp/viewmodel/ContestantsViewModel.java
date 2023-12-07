package com.lorbeer.refugeeapp.viewmodel;

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




    public void queryAllContestants() {
        contestantsRepository = ContestantsRepository.getINSTANCE();
        allContestants = contestantsRepository.getAllContestants();
    }

    public LiveData<List<Contestants>> getAllContestants() {
        return allContestants;
    }

    public void queryContestantByMail(String email) {
        contestantsRepository = ContestantsRepository.getINSTANCE();
        contestantsByMail = contestantsRepository.getContestantbyMail(email);
    }

    public LiveData<Contestants> getContestantByMail() {
        return contestantsByMail;
    }

    public void queryContestantsForCourses(Integer id) {
        contestantsRepository = ContestantsRepository.getINSTANCE();
        contestantsForCourse = contestantsRepository.getContestantsForCourse(id);
    }

    public LiveData<List<Contestants>> getContestantsForCourses() {
        return contestantsForCourse;
    }
}
