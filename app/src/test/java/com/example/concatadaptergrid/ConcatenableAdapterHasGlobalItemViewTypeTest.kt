package com.example.concatadaptergrid

import org.assertj.core.api.Assertions
import org.junit.Test

class ConcatenableAdapterHasGlobalItemViewTypeTest {

    @Test
    fun adapter0_firstItem() {
        // Assemble
        val adapter = MockAdapter1(concatAdapterIndex = 0)

        // Act
        val result = adapter.hasGlobalViewItemType(globalItemViewType = 100)

        // Assert
        Assertions.assertThat(result).isTrue()
    }

    @Test
    fun adapter0_item1() {
        // Assemble
        val adapter = MockAdapter1(concatAdapterIndex = 0)

        // Act
        val result = adapter.hasGlobalViewItemType(globalItemViewType = 101)

        // Assert
        Assertions.assertThat(result).isTrue()
    }

    @Test
    fun adapter0_lastItem() {
        // Assemble
        val adapter = MockAdapter1(concatAdapterIndex = 0)

        // Act
        val result = adapter.hasGlobalViewItemType(globalItemViewType = 199)

        // Assert
        Assertions.assertThat(result).isTrue()
    }

    @Test
    fun incorrectItemType_adapter0() {
        // Assemble
        val adapter = MockAdapter1(concatAdapterIndex = 0)

        // Act
        val result = adapter.hasGlobalViewItemType(globalItemViewType = 200)

        // Assert
        Assertions.assertThat(result).isFalse()
    }

    @Test
    fun adapter1_firstItem() {
        // Assemble
        val adapter = MockAdapter1(concatAdapterIndex = 1)

        // Act
        val result = adapter.hasGlobalViewItemType(globalItemViewType = 200)

        // Assert
        Assertions.assertThat(result).isTrue()
    }

    @Test
    fun adapter1_item1() {
        // Assemble
        val adapter = MockAdapter1(concatAdapterIndex = 1)

        // Act
        val result = adapter.hasGlobalViewItemType(globalItemViewType = 201)

        // Assert
        Assertions.assertThat(result).isTrue()
    }

    @Test
    fun adapter1_lastItem() {
        // Assemble
        val adapter = MockAdapter1(concatAdapterIndex = 1)

        // Act
        val result = adapter.hasGlobalViewItemType(globalItemViewType = 299)

        // Assert
        Assertions.assertThat(result).isTrue()
    }

    @Test
    fun incorrectItemType_adapter1() {
        // Assemble
        val adapter = MockAdapter1(concatAdapterIndex = 1)

        // Act
        val result = adapter.hasGlobalViewItemType(globalItemViewType = 300)

        // Assert
        Assertions.assertThat(result).isFalse()
    }

    class MockAdapter1(
        override val concatAdapterIndex: Int
    ) : ConcatenableAdapter
}
