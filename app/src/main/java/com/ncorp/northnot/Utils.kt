import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ncorp.northnot.NotClass

object SharedPrefUtils {

    private const val PREFS_NAME = "MY_PREFS"
    private const val KEY_NOT_LIST = "NOT_LIST"

    fun saveNotes(context: Context, notes: List<NotClass>) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        // Listeyi JSON stringe çeviriyoruz
        val jsonString = Gson().toJson(notes)

        editor.putString(KEY_NOT_LIST, jsonString)
        editor.apply()
    }

    fun loadNotes(context: Context): MutableList<NotClass> {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val jsonString = sharedPref.getString(KEY_NOT_LIST, null) ?: return mutableListOf()

        // JSON stringi listeye çeviriyoruz
        val type = object : TypeToken<MutableList<NotClass>>() {}.type
        return Gson().fromJson(jsonString, type)
    }

}
