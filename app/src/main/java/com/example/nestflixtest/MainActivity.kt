package com.example.nestflixtest


import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.example.nestflixtest.ui.theme.NestflixTestTheme
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaItem.LiveConfiguration
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.rtsp.RtspMediaSource
import com.google.android.exoplayer2.ui.StyledPlayerControlView
import com.google.android.exoplayer2.ui.StyledPlayerView
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import org.videolan.libvlc.util.VLCVideoLayout



class MainActivity : ComponentActivity(), MediaPlayer.EventListener {

    private val testurl = "rtsp://rtsp.stream/pattern"
    private val raspberry = "rtsp://10.0.0.134:3366/stream1"

    private var libVlc: LibVLC? = null
    private var mediaPlayer: MediaPlayer? = null
    private var videoLayout: VLCVideoLayout? = null
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_main)
        libVlc = LibVLC(this)
        mediaPlayer = MediaPlayer(libVlc)
        mediaPlayer!!.setEventListener(this)
        videoLayout = findViewById(R.id.videoLayout)
        progressDialog = ProgressDialog() //this
        progressDialog!!.ProgressDialog(this)  //hinzugefügt*/
    }


    override fun onEvent(event: MediaPlayer.Event) {
        if (event.type == MediaPlayer.Event.Buffering) {
            if (event.buffering == 100f) {
                progressDialog?.hide() //                progressDialog.hide()

            } else {
                progressDialog?.show()    // progressDialog.show()
            }
        }
    }
   override fun onStart() {
        super.onStart()
        mediaPlayer!!.attachViews(videoLayout!!, null, false, false)
        val media = Media(libVlc, Uri.parse(testurl))
        media.setHWDecoderEnabled(true, false)
        media.addOption(":network-caching=600")
        mediaPlayer!!.media = media
        media.release()
        mediaPlayer!!.play()
    }

    override fun onStop() {
        super.onStop()
        mediaPlayer!!.stop()
        mediaPlayer!!.detachViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer!!.release()
        libVlc!!.release()
    }

}


@Composable
fun VideoPlayer(){
    val streamVideo = "rtsp://10.0.0.134:8554/stream1"
    val testVideo = "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mp4"
    val context = LocalContext.current
    val player = ExoPlayer.Builder(context).build()
    val playerView = StyledPlayerView(context)
  //  https://exoplayer.dev/doc/reference/com/google/android/exoplayer2/ui/StyledPlayerView.html
    playerView.setShowFastForwardButton(false)
    playerView.setShowNextButton(false)
    playerView.setShowPreviousButton(false)
    playerView.setShowRewindButton(false)
    playerView.hideController()


    val videoSource = RtspMediaSource.Factory().setDebugLoggingEnabled(true)
        .createMediaSource(MediaItem.fromUri(testVideo))
    val playWhenReady by rememberSaveable {
        mutableStateOf(true)
    }

    player.setMediaSource(videoSource)

    //// Bind the player to the view.

    playerView.player = player

    LaunchedEffect(player) {
        player.prepare()
        player.playWhenReady = playWhenReady

    }
    AndroidView(factory = {
        playerView
    })
}



@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable
fun TextViewCompose() {
    AndroidView(factory = { context ->

        TextView(context).apply {
            text = "Hello from View"
        }
    })
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NestflixTestTheme {
        Greeting("Android")
    }
}