import android.os.AsyncTask
import com.example.firebaseprojekt2.DataModel.DatabaseModel
import java.net.URL
import java.net.URLStreamHandler
import java.util.ArrayList

class productDownload(delegate: AsyncResponse) : AsyncTask<String, Void, String>() {

    interface AsyncResponse {
        fun processFinish(output: String?)
    }
    var delegate: AsyncResponse? = null
    init{
        this.delegate = delegate
    }

    override fun doInBackground(vararg p0: String?): String? {
        var response:String?
        var productAdress = p0[0]
        try{
            response = URL(productAdress)
                .readText(Charsets.UTF_8)

        }catch (e: Exception){
            print(e)
            if(e is java.net.ProtocolException) response = "EXCEPTION"
            else if(e is java.net.ConnectException) response = "CONNECTION"
            else response = null
        }
        return response
    }

    override fun onPostExecute(result: String?) {
            delegate!!.processFinish(result)
    }
}