package com.talentstream.TestServices;



import com.talentstream.entity.*;
import com.talentstream.exception.CustomException;
import com.talentstream.repository.*;
import com.talentstream.service.SavedJobService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SavedJobServiceTest {

    @InjectMocks
    private SavedJobService savedJobService;

    @Mock
    private SavedJobRepository savedJobRepository;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private RegisterRepository applicantRepository;

    @Mock
    private ApplyJobRepository applyJobRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveJobForApplicant_Success() throws Exception {
        long applicantId = 1L;
        long jobId = 2L;
        Applicant applicant = new Applicant();
        Job job = new Job();

        when(applicantRepository.findById(applicantId)).thenReturn(applicant);
        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));
        when(savedJobRepository.existsByApplicantAndJob(applicant, job)).thenReturn(false);

        assertDoesNotThrow(() -> savedJobService.saveJobForApplicant(applicantId, jobId));
        verify(savedJobRepository).save(any(SavedJob.class));
    }

    @Test
    void testSaveJobForApplicant_AlreadySaved() {
        long applicantId = 1L;
        long jobId = 2L;
        Applicant applicant = new Applicant();
        Job job = new Job();

        when(applicantRepository.findById(applicantId)).thenReturn(applicant);
        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));
        when(savedJobRepository.existsByApplicantAndJob(applicant, job)).thenReturn(true);

        CustomException ex = assertThrows(CustomException.class, () ->
                savedJobService.saveJobForApplicant(applicantId, jobId));
        assertEquals("Job has already been saved by the applicant", ex.getMessage());
    }

    @Test
    void testGetSavedJobsForApplicant_WithResults() {
        long applicantId = 1L;
        int page = 0;
        int size = 2;

        List<Long> savedJobIds = List.of(10L, 20L);
        List<Job> jobList = Arrays.asList(new Job(), new Job());
        Pageable pageable = PageRequest.of(page, size);
        Page<Job> jobPage = new PageImpl<>(jobList, pageable, jobList.size());

        when(savedJobRepository.findSavedJobIdsByApplicantId(applicantId)).thenReturn(savedJobIds);
        when(jobRepository.findJobsByIds(savedJobIds, pageable)).thenReturn(jobPage);

        Page<Job> result = savedJobService.getSavedJobsForApplicant(applicantId, page, size);
        assertEquals(2, result.getContent().size());
    }

    @Test
    void testCountSavedJobsForApplicant_ValidApplicant() {
        long applicantId = 1L;
        when(applicantRepository.existsById(applicantId)).thenReturn(true);
        when(savedJobRepository.countByApplicantId(applicantId)).thenReturn(3L);

        long count = savedJobService.countSavedJobsForApplicant(applicantId);
        assertEquals(3L, count);
    }

    @Test
    void testCountSavedJobsForApplicant_InvalidApplicant() {
        long applicantId = 1L;
        when(applicantRepository.existsById(applicantId)).thenReturn(false);

        CustomException ex = assertThrows(CustomException.class, () ->
                savedJobService.countSavedJobsForApplicant(applicantId));
        assertEquals("Applicant not found", ex.getMessage());
    }

    @Test
    void testDeleteSavedJobForApplicant_Success() {
        long applicantId = 1L;
        long jobId = 2L;

        when(applicantRepository.existsById(applicantId)).thenReturn(true);
        when(jobRepository.existsById(jobId)).thenReturn(true);
        when(savedJobRepository.deleteByApplicantIdAndJobId(applicantId, jobId)).thenReturn(1);

        int deleted = savedJobService.deleteSavedJobForApplicant(applicantId, jobId);
        assertEquals(1, deleted);
    }

    @Test
    void testDeleteSavedJobForApplicant_ApplicantNotFound() {
        long applicantId = 1L;
        long jobId = 2L;

        when(applicantRepository.existsById(applicantId)).thenReturn(false);

        CustomException ex = assertThrows(CustomException.class, () ->
                savedJobService.deleteSavedJobForApplicant(applicantId, jobId));
        assertEquals("Applicant not found", ex.getMessage());
    }
}
