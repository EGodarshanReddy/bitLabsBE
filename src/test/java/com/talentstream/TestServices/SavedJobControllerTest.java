package com.talentstream.TestServices;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.talentstream.controller.SavedJobController;
import com.talentstream.service.SavedJobService;

public class SavedJobControllerTest {

    @Mock
    private SavedJobService savedJobService;

    @InjectMocks
    private SavedJobController savedJobController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveJobForApplicant_Success() throws Exception {
        long applicantId = 1L;
        long jobId = 101L;

        doNothing().when(savedJobService).saveJobForApplicant(applicantId, jobId);

        ResponseEntity<String> response = savedJobController.saveJobForApplicant(applicantId, jobId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Job saved successfully for the applicant.", response.getBody());

        verify(savedJobService, times(1)).saveJobForApplicant(applicantId, jobId);
    }



    @Test
    void testSaveJobForApplicant_GenericException() throws Exception {
        long applicantId = 1L;
        long jobId = 101L;

        doThrow(new RuntimeException("DB error")).when(savedJobService).saveJobForApplicant(applicantId, jobId);

        ResponseEntity<String> response = savedJobController.saveJobForApplicant(applicantId, jobId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error saving job for the applicant.", response.getBody());

        verify(savedJobService, times(1)).saveJobForApplicant(applicantId, jobId);
    }
}
