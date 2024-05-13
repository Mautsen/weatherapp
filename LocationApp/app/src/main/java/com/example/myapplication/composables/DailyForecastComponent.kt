import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.model.DailyForecastResponse
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun DailyForecastComponent(dailyForecast: DailyForecastResponse?) {
    LazyRow {
        items(dailyForecast?.daily?.time ?: emptyList()) { dateString ->
                val date = LocalDate.parse(dateString)
                val formattedDate = date.format(DateTimeFormatter.ofPattern("d.M."))
                val index = dailyForecast?.daily?.time?.indexOf(dateString) ?: 0
                val maxTemperature = dailyForecast?.daily?.maxTemperatures?.get(index) ?: 0.0
                val minTemperature = dailyForecast?.daily?.minTemperatures?.get(index) ?: 0.0
                val weatherCode = dailyForecast?.daily?.weatherCodes?.get(index) ?: 0

                WeatherCard(formattedDate, maxTemperature, minTemperature, weatherCode)
        }
    }
}

@Composable
fun WeatherCard(
    formattedDate: String,
    maxTemperature: Double,
    minTemperature: Double,
    weatherCode: Int
) {
    ElevatedCard(
        modifier = Modifier.padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "$formattedDate")
            Text(text = "Max: $maxTemperature°C")
            Text(text = "Min: $minTemperature°C")

            // Show weather icon based on weatherCode
            val iconPainter = when (weatherCode) {
                0 -> painterResource(R.drawable.sunny)
                1, 2 -> painterResource(R.drawable.partly_cloudy)
                3 -> painterResource(R.drawable.cloudy)
                51, 53, 55, 56, 57, 61, 63, 65, 66, 67, 80, 81, 82 -> painterResource(R.drawable.rainy)
                71, 73, 75, 77, 85, 86 -> painterResource(R.drawable.snowy)
                95, 96, 99 -> painterResource(R.drawable.stormy)
                45, 48 -> painterResource(R.drawable.foggy)
                else -> painterResource(R.drawable.default_image)
            }
            Image(painter = iconPainter, contentDescription = "Weather Icon",  modifier = Modifier.size(80.dp))
        }
    }
}
