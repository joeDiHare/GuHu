/*
 * Copyright (c) 2014 HollowSoft @IgorMorais
 *
 * Licensed under the Apache License, Version 2.0 (the �License�);
 * you may not use this file except in compliance with the License.
 *
 *          You may obtain a copy of the License at
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an �AS IS� BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.plusinfosys.guhunaples.widgets;

/**
 * Listener invoked when the drawer is scrolled.
 *
 * <p> This interface has ported and improved from the Android Open Source Project.
 *
 * @see <a href="http://http://developer.android.com/reference/android/widget/SlidingDrawer.OnDrawerScrollListener.html">
 *      SlidingDrawer.OnDrawerScrollListener</a>
 *
 * @author Igor Morais
 * @author mor41s.1gor@gmail.com
 */
public interface OnDrawerScrollListener {

    /**
     * Invoked when the user starts dragging/flinging the drawer's handle.
     */
     void onScrollStarted();

    /**
     * Invoked when the user stops dragging/flinging the drawer's handle.
     */
     void onScrollEnded();
}
