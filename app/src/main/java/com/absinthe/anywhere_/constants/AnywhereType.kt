package com.absinthe.anywhere_.constants

object AnywhereType {

  object Card {
    const val NOT_CARD = -1
    const val URL_SCHEME = 0
    const val ACTIVITY = 1
    const val QR_CODE = 3
    const val IMAGE = 4
    const val SHELL = 5
    const val FILE = 7


  }


  object Category {
    const val DEFAULT_CATEGORY = "Default"
  }

  object Page {
    const val CARD_PAGE = 0
  }

  object Flags {
    const val FLAG_EXEC_WITH_ROOT = 0x01
    const val FLAG_BRIGHT_WHEN_SHOW_IMAGE = 0x02
  }
}
