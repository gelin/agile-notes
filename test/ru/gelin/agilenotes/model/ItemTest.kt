/*
 * Agile Notes. Flexible Android notes/task manager.
 * Copyright (C) 2015  Denis Nelubin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ru.gelin.agilenotes.model

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.text.Regex

public class ItemTest {

    Test fun testId() {
        var item = Item()
        assertNotNull(item.id)
        assertTrue(Regex("[0-9A-F]{8}-[0-9A-F]{4}-[0-9A-F]{4}-[0-9A-F]{4}-[0-9A-F]{12}").matches(item.id.toString()))
    }

    Test fun testHead() {
        val item = Item()
        item.head = "MyHead"
        assertEquals("MyHead", item.head)
    }

    Test fun testBody() {
        val item = Item()
        item.body = "MyBody"
        assertEquals("MyBody", item.body)
    }

}
