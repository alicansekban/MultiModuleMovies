package com.alican.data.service

import com.alican.data.utils.ResultWrapper
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ApiServiceTest {

    private lateinit var fakeApiService: FakeApiService

    @Before
    fun setup() {
        fakeApiService = FakeApiService()
    }

    @Test
    fun `getUpComingMovies returns success when no error`() = runBlocking {
        // Given
        val page = 1

        // When
        val result = fakeApiService.getUpComingMovies(page)

        // Then
        assertTrue(result is ResultWrapper.Success)
        val successResult = result as ResultWrapper.Success
        assertEquals(page, successResult.value.page)
        assertEquals(2, successResult.value.results?.size)
        assertEquals(100, successResult.value.total_results)
    }

    @Test
    fun `getUpComingMovies returns error when shouldReturnError is true`() = runBlocking {
        // Given
        fakeApiService.shouldReturnError = true
        fakeApiService.errorCode = 404
        fakeApiService.errorMessage = "Not found"

        // When
        val result = fakeApiService.getUpComingMovies(1)

        // Then
        assertTrue(result is ResultWrapper.Error)
        val errorResult = result as ResultWrapper.Error
        assertEquals("Not found", errorResult.message)
    }

    @Test
    fun `getPopularMovies returns success with correct data`() = runBlocking {
        // Given
        val page = 2

        // When
        val result = fakeApiService.getPopularMovies(page)

        // Then
        assertTrue(result is ResultWrapper.Success)
        val successResult = result as ResultWrapper.Success
        assertEquals(page, successResult.value.page)
        assertEquals("Test Movie 1", successResult.value.results?.first()?.title)
    }

    @Test
    fun `getTopRatedMovies returns success`() = runBlocking {
        // When
        val result = fakeApiService.getTopRatedMovies(1)

        // Then
        assertTrue(result is ResultWrapper.Success)
        val successResult = result as ResultWrapper.Success
        assertEquals(10, successResult.value.total_pages)
    }

    @Test
    fun `getNowPlayingMovies returns success`() = runBlocking {
        // When
        val result = fakeApiService.getNowPlayingMovies(1)

        // Then
        assertTrue(result is ResultWrapper.Success)
        val successResult = result as ResultWrapper.Success
        assertNotNull(successResult.value.results)
        assertEquals(2, successResult.value.results?.size)
    }

    @Test
    fun `getMovieDetail returns success with correct movie id`() = runBlocking {
        // Given
        val movieId = 123

        // When
        val result = fakeApiService.getMovieDetail(movieId)

        // Then
        assertTrue(result is ResultWrapper.Success)
        val successResult = result as ResultWrapper.Success
        assertEquals(movieId, successResult.value.id)
        assertEquals("Test Movie Detail", successResult.value.title)
        assertEquals(120, successResult.value.runtime)
    }

    @Test
    fun `getMovieDetail returns error when shouldReturnError is true`() = runBlocking {
        // Given
        fakeApiService.shouldReturnError = true

        // When
        val result = fakeApiService.getMovieDetail(123)

        // Then
        assertTrue(result is ResultWrapper.Error)
    }

    @Test
    fun `getMovieCredits returns success with movie id`() = runBlocking {
        // Given
        val movieId = 456

        // When
        val result = fakeApiService.getMovieCredits(movieId)

        // Then
        assertTrue(result is ResultWrapper.Success)
        val successResult = result as ResultWrapper.Success
        assertEquals(movieId, successResult.value.id)
        assertNotNull(successResult.value.cast)
        assertNotNull(successResult.value.crew)
    }

    @Test
    fun `getMovieReviews returns success with correct pagination`() = runBlocking {
        // Given
        val movieId = 789
        val page = 3

        // When
        val result = fakeApiService.getMovieReviews(movieId, page)

        // Then
        assertTrue(result is ResultWrapper.Success)
        val successResult = result as ResultWrapper.Success
        assertEquals(page, successResult.value.page)
    }

    @Test
    fun `searchMovies returns filtered results when query matches`() = runBlocking {
        // Given
        val query = "Test Movie 1"
        val page = 1

        // When
        val result = fakeApiService.searchMovies(query, page)

        // Then
        assertTrue(result is ResultWrapper.Success)
        val successResult = result as ResultWrapper.Success
        assertEquals(1, successResult.value.results?.size)
        assertEquals("Test Movie 1", successResult.value.results?.first()?.title)
    }

    @Test
    fun `searchMovies returns all results when query is empty`() = runBlocking {
        // Given
        val query = ""
        val page = 1

        // When
        val result = fakeApiService.searchMovies(query, page)

        // Then
        assertTrue(result is ResultWrapper.Success)
        val successResult = result as ResultWrapper.Success
        assertEquals(2, successResult.value.results?.size)
    }

    @Test
    fun `searchMovies returns empty results when query doesn't match`() = runBlocking {
        // Given
        val query = "Non-existent Movie"
        val page = 1

        // When
        val result = fakeApiService.searchMovies(query, page)

        // Then
        assertTrue(result is ResultWrapper.Success)
        val successResult = result as ResultWrapper.Success
        assertEquals(0, successResult.value.results?.size)
    }

    @Test
    fun `getMovieImages returns success with movie id`() = runBlocking {
        // Given
        val movieId = 999

        // When
        val result = fakeApiService.getMovieImages(movieId)

        // Then
        assertTrue(result is ResultWrapper.Success)
        val successResult = result as ResultWrapper.Success
        assertEquals(movieId, successResult.value.id)
        assertNotNull(successResult.value.backdrops)
        assertNotNull(successResult.value.logos)
        assertNotNull(successResult.value.posters)
    }

    @Test
    fun `all methods return error when shouldReturnError is true`() = runBlocking {
        // Given
        fakeApiService.shouldReturnError = true
        fakeApiService.errorCode = 500
        fakeApiService.errorMessage = "Internal Server Error"

        // When & Then
        val upcomingResult = fakeApiService.getUpComingMovies(1)
        val popularResult = fakeApiService.getPopularMovies(1)
        val topRatedResult = fakeApiService.getTopRatedMovies(1)
        val nowPlayingResult = fakeApiService.getNowPlayingMovies(1)
        val detailResult = fakeApiService.getMovieDetail(1)
        val creditsResult = fakeApiService.getMovieCredits(1)
        val reviewsResult = fakeApiService.getMovieReviews(1, 1)
        val searchResult = fakeApiService.searchMovies("test", 1)
        val imagesResult = fakeApiService.getMovieImages(1)

        // All should be GenericError
        listOf(
            upcomingResult, popularResult, topRatedResult, nowPlayingResult,
            detailResult, creditsResult, reviewsResult, searchResult, imagesResult
        ).forEach { result ->
            assertTrue(result is ResultWrapper.Error)
            val errorResult = result as ResultWrapper.Error
            assertEquals("Internal Server Error", errorResult.message)
        }
    }
}