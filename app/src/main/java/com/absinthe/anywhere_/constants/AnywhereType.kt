package com.absinthe.anywhere_.constants

object AnywhereType {

  object Card {
    const val NOT_CARD = -1
    const val URL_SCHEME = 0
    const val ACTIVITY = 1
    const val MINI_PROGRAM = 2
    const val QR_CODE = 3
    const val IMAGE = 4
    const val SHELL = 5
    const val SWITCH_SHELL = 6
    const val FILE = 7
    const val BROADCAST = 8
    const val WORKFLOW = 9
    const val ACCESSIBILITY = 10


  }


  object Category {
    const val DEFAULT_CATEGORY = "Default"
  }

  object Page {
    const val CARD_PAGE = 0
    const val WEB_PAGE = 1
  }

  object Flags {
    const val FLAG_EXEC_WITH_ROOT = 0x01
    const val FLAG_BRIGHT_WHEN_SHOW_IMAGE = 0x02
  }
}
