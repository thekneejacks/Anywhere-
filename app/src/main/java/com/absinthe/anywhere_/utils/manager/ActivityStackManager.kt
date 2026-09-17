package com.absinthe.anywhere_.utils.manager

import androidx.appcompat.app.AppCompatActivity
import java.lang.ref.WeakReference
import java.util.Stack

object ActivityStackManager {
  /***
   * Get stack
   *
   * @return Activity stack
   */
  /**
   * Activity Stack
   */
  private var stack: Stack<WeakReference<AppCompatActivity>> = Stack()

  /***
   * Size of Activities
   *
   * @return Size of Activities
   */

  /**
   * Add Activity to stack
   */
  fun addActivity(activity: WeakReference<AppCompatActivity>) {
    stack.add(activity)
  }

  /**
   * Delete Activity
   *
   * @param activity Weak Reference of Activity
   */
  fun removeActivity(activity: WeakReference<AppCompatActivity>) {
    stack.remove(activity)
  }

}
