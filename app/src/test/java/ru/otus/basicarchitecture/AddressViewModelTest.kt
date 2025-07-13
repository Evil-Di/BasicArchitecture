package ru.otus.basicarchitecture

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import ru.otus.basicarchitecture.data.WizardCache
import ru.otus.basicarchitecture.net.NetService
import ru.otus.basicarchitecture.net.Suggestion
import ru.otus.basicarchitecture.net.Suggestions
import ru.otus.basicarchitecture.ui.address.AddressViewModel

class AddressViewModelTest {
    private val service: NetService = mock()
    private val wizardCache: WizardCache = mock()
    private val viewModel = AddressViewModel(wizardCache, service)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `should emit showNext as false`() = runTest {
        //given:
        val expected = true
        whenever(wizardCache.address).thenReturn("Address")
        //when:
        viewModel.getFromCache()
        val actual = viewModel.showNext.value
        //then:
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `should emit showNext as true`() = runTest {
        //given:
        val expected = false
        whenever(wizardCache.address).thenReturn("")
        //when:
        viewModel.getFromCache()
        val actual = viewModel.showNext.value
        //then:
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `should emit suggestions with data`() = runTest {
        //given:
        val mockData = Suggestions(
            listOf(
                Suggestion("Suggestion 1"),
                Suggestion("Suggestion 2")
            )
        )
        val expected: Suggestions = mockData
        whenever(service.getSuggestions(any())).thenReturn(mockData)
        //when:
        viewModel.getSuggestions("test")
        val actual = viewModel.suggestions.value
        //then:
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `should emit suggestions with null value`() = runTest {
        //given:
        val mockData = Suggestions(
            listOf(
                Suggestion("Suggestion 1"),
                Suggestion("Suggestion 2")
            )
        )
        val expected = null
        whenever(service.getSuggestions(any())).thenReturn(mockData)
        //when:
        viewModel.setAddress("test")
        viewModel.getSuggestions("test")
        viewModel.getSuggestions("test")
        val actual = viewModel.suggestions.value
        //then:
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `suggestions should not change on net error`() = runTest {
        //given:
        val mockData = Suggestions(listOf(Suggestion("Suggestion")))
        val expected: Suggestions = mockData
        whenever(service.getSuggestions("test1")).thenReturn(mockData)
        whenever(service.getSuggestions("test2")).thenThrow(RuntimeException("Network error"))
        //when:
        viewModel.getSuggestions("test1")
        viewModel.getSuggestions("test2")
        val actual = viewModel.suggestions.value
        //then:
        Assert.assertEquals(expected, actual)
    }
}