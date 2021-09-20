package com.example.concatadaptergrid

import org.assertj.core.api.Assertions
import org.junit.Test

class ConcatenableAdapterResolveViewItemTypeTest {

    @Test
    fun adapter0_item0() {
        // Assemble
        val adapter = MockAdapter1(concatAdapterIndex = 0)

        // Act
        val result = adapter.resolveGlobalViewItemType(globalItemViewType = 100)

        // Assert
        Assertions.assertThat(result).isEqualTo(0)
    }

    @Test
    fun adapter0_item1() {
        // Assemble
        val adapter = MockAdapter1(concatAdapterIndex = 0)

        // Act
        val result = adapter.resolveGlobalViewItemType(globalItemViewType = 101)

        // Assert
        Assertions.assertThat(result).isEqualTo(1)
    }

    @Test
    fun adapter1_item0() {
        // Assemble
        val adapter = MockAdapter1(concatAdapterIndex = 1)

        // Act
        val result = adapter.resolveGlobalViewItemType(globalItemViewType = 200)

        // Assert
        Assertions.assertThat(result).isEqualTo(0)
    }

    @Test
    fun adapter1_item1() {
        // Assemble
        val adapter = MockAdapter1(concatAdapterIndex = 1)

        // Act
        val result = adapter.resolveGlobalViewItemType(globalItemViewType = 201)

        // Assert
        Assertions.assertThat(result).isEqualTo(1)
    }

    @Test
    fun adapter2_item0() {
        // Assemble
        val adapter = MockAdapter1(concatAdapterIndex = 2)

        // Act
        val result = adapter.resolveGlobalViewItemType(globalItemViewType = 300)

        // Assert
        Assertions.assertThat(result).isEqualTo(0)
    }

    @Test
    fun adapter2_item1() {
        // Assemble
        val adapter = MockAdapter1(concatAdapterIndex = 2)

        // Act
        val result = adapter.resolveGlobalViewItemType(globalItemViewType = 301)

        // Assert
        Assertions.assertThat(result).isEqualTo(1)
    }

    class MockAdapter1(
        override val concatAdapterIndex: Int
    ) : ConcatenableAdapter
}
