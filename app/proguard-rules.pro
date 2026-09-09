# This is a configuration file for ProGuard.
# http://proguard.sourceforge.net/index.html#manual/usage.html
#
# Starting with version 2.2 of the Android plugin for Gradle, this file is distributed together with
# the plugin and unpacked at build-time. The files in $ANDROID_HOME are no longer maintained and
# will be ignored by new version of the Android plugin for Gradle.

#-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

# Optimization is turned off by default. Dex does not like code run
# through the ProGuard optimize and preverify steps (and performs some
# of these optimizations on its own).
# -dontoptimize
-optimizationpasses 5
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*,!code/allocation/variable,!class/unboxing/enum
#-obfuscationdictionary dictionary.txt
#-classobfuscationdictionary dictionary.txt
#-packageobfuscationdictionary dictionary.txt
-repackageclasses com.absinthe.anywhere_

-dontpreverify

# Preserve some attributes that may be required for reflection.

# The support libraries contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version. We know about them, and they are safe.
-dontnote android.support.**
-dontwarn android.support.**

-dontwarn javax.annotation.**

-dontwarn org.xmlpull.v1.XmlPullParser
-dontwarn org.xmlpull.v1.XmlSerializer
