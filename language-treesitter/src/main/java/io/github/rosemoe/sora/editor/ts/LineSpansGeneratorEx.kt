/*******************************************************************************
 *    sora-editor - the awesome code editor for Android
 *    https://github.com/Rosemoe/sora-editor
 *    Copyright (C) 2020-2022  Rosemoe
 *
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public
 *     License as published by the Free Software Foundation; either
 *     version 2.1 of the License, or (at your option) any later version.
 *
 *     This library is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with this library; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 *     USA
 *
 *     Please contact Rosemoe by email 2073412493@qq.com if you need
 *     additional information or have any questions
 ******************************************************************************/

package io.github.rosemoe.sora.editor.ts

import android.util.Log
import com.itsaky.androidide.treesitter.TSLanguage
import com.itsaky.androidide.treesitter.TSQuery
import com.itsaky.androidide.treesitter.TSQueryCursor
import com.itsaky.androidide.treesitter.TSTree

class LineSpansGeneratorEx {

    fun test(language: TSLanguage, scmSource: String, tree: TSTree, start: Int, end: Int) {
        val query = TSQuery(language, scmSource)
        val cursor = TSQueryCursor()
        cursor.setByteRange(start, end)
        cursor.matchLimit = 114514
        cursor.exec(query, tree.rootNode)
        var match = cursor.nextMatch()
        while (match != null) {
            match.captures.forEach {
                Log.d("Test", "Captured: ${it.node.nodeString}\nPattern type: ${query.getCaptureNameForId(it.index)}")
            }
            match = cursor.nextMatch()
        }
    }

}