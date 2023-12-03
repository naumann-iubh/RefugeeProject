package com.lorbeer.refugeeapp;

import org.junit.Test;

import static org.junit.Assert.*;

import com.lorbeer.refugeeapp.domain.Course;
import com.lorbeer.refugeeapp.repository.RefugeeRepository;

import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    RefugeeRepository repository = RefugeeRepository.getInstance();
    @Test
    public void test() {


        List<Course> result = repository.getAllCourses();

        result.forEach(a -> System.out.println(a.toString()));

        assertFalse(result.isEmpty());

    }
}