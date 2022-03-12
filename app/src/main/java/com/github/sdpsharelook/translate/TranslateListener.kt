package com.github.sdpsharelook.translate

/** The listener interface for receiving translate from Translator.
 * The class that is interested in processing a text translation either implements this interface.
 */
interface TranslateListener {

    /** Invoked when an error is raised.
     * @param e : Exception | The raised exception
     */
    fun onError(e: Exception)

    /** Invoked when the text is translated.
     * @param translatedText : String | The translated text
     */
    fun onTranslated(translatedText : String)
}