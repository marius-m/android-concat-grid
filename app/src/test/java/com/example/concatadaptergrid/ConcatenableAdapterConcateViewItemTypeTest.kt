package com.example.concatadaptergrid

import org.assertj.core.api.Assertions
import org.junit.Test

class ConcatenableAdapterConcateViewItemTypeTest {

    @Test
    fun adapter0_item0() {
        // Assemble
        val adapter = MockAdapter1(concatAdapterIndex = 0)

        // Act
        val result = adapter.globalViewItemType(localItemViewType = 0)

        // Assert
        Assertions.assertThat(result).isEqualTo(100)
    }

    @Test
    fun adapter0_item1() {
        // Assemble
        val adapter = MockAdapter1(concatAdapterIndex = 0)

        // Act
        val result = adapter.globalViewItemType(localItemViewType = 1)

        // Assert
        Assertions.assertThat(result).isEqualTo(101)
    }

    @Test
    fun adapter1_item0() {
        // Assemble
        val adapter = MockAdapter1(concatAdapterIndex = 1)

        // Act
        val result = adapter.globalViewItemType(localItemViewType = 0)

        // Assert
        Assertions.assertThat(result).isEqualTo(200)
    }

    @Test
    fun adapter1_item1() {
        // Assemble
        val adapter = MockAdapter1(concatAdapterIndex = 1)

        // Act
        val result = adapter.globalViewItemType(localItemViewType = 1)

        // Assert
        Assertions.assertThat(result).isEqualTo(201)
    }

    @Test
    fun adapter2_item0() {
        // Assemble
        val adapter = MockAdapter1(concatAdapterIndex = 2)

        // Act
        val result = adapter.globalViewItemType(localItemViewType = 0)

        // Assert
        Assertions.assertThat(result).isEqualTo(300)
    }

    @Test
    fun adapter2_item1() {
        // Assemble
        val adapter = MockAdapter1(concatAdapterIndex = 2)

        // Act
        val result = adapter.globalViewItemType(localItemViewType = 1)

        // Assert
        Assertions.assertThat(result).isEqualTo(301)
    }

    class MockAdapter1(
        override val concatAdapterIndex: Int
    ) : ConcatenableAdapter
}
