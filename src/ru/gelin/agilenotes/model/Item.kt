/*
 * Agile GTD. Flexible Android implementation of GTD.
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

import java.util
import java.util.*

/**
 * The basic model Item.
 * Contains head, body and children list,
 */
public class Item {

    val id : UUID = UUID.randomUUID()

    var head : String = ""

    var body : String = ""

    val children : List<Item> = ChildrenList<Item>()

}

class ChildrenList<Item> : List<Item> {

    override fun size(): Int {
        throw UnsupportedOperationException()
    }

    override fun isEmpty(): Boolean {
        throw UnsupportedOperationException()
    }

    override fun contains(o: Any?): Boolean {
        throw UnsupportedOperationException()
    }

    override fun iterator(): Iterator<Item> {
        throw UnsupportedOperationException()
    }

    override fun containsAll(c: Collection<Any?>): Boolean {
        throw UnsupportedOperationException()
    }

    override fun get(index: Int): Item {
        throw UnsupportedOperationException()
    }

    override fun indexOf(o: Any?): Int {
        throw UnsupportedOperationException()
    }

    override fun lastIndexOf(o: Any?): Int {
        throw UnsupportedOperationException()
    }

    override fun listIterator(): ListIterator<Item> {
        throw UnsupportedOperationException()
    }

    override fun listIterator(index: Int): ListIterator<Item> {
        throw UnsupportedOperationException()
    }

    override fun subList(fromIndex: Int, toIndex: Int): List<Item> {
        throw UnsupportedOperationException()
    }

}
