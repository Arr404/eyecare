package com.total.eyecare.ui.lokasi

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*
import com.total.eyecare.R
import com.total.eyecare.classes.lokasi

class lokasiActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var database: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lokasi)
        val backButton = findViewById<ImageButton>(R.id.back_lokasi)
        backButton.setOnClickListener{
            finish()
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onBackPressed() {
        finish()
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        getDataLocation()

        mMap.setOnMarkerClickListener { marker ->
//            val intentDetail = Intent(this@MapsStoryActivity, DetailActivity::class.java)
//            intentDetail.putExtra("username", marker.snippet)
            Log.e("id Maps",marker.id)
            marker.snippet?.let { Log.e("snippets Maps", it) }
//            startActivity(intentDetail)
//            finish()
            true
        }
    }
    private fun getDataLocation(){
        val daftarinfo = mutableListOf<lokasi>()
        database = FirebaseDatabase.getInstance().reference

        database?.child("lokasi")?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                /**
                 * Saat ada data baru, masukkan datanya ke ArrayList
                 */
                //daftarprofil
                for (noteDataSnapshot in dataSnapshot.children) {
                    /**
                     * Mapping data pada DataSnapshot ke dalam object Barang
                     * Dan juga menyimpan primary key pada object Barang
                     * untuk keperluan Edit dan Delete data
                     */
                    val info: lokasi? = noteDataSnapshot.getValue(lokasi::class.java)
                    /**
                     * Menambahkan object Barang yang sudah dimapping
                     * ke dalam ArrayList
                     */

                    if (info != null) {
                        daftarinfo += (info)

                    }
                }
                Log.e("on data change",daftarinfo.toString())
                addMarker(daftarinfo)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                /**
                 * Kode ini akan dipanggil ketika ada error dan
                 * pengambilan data gagal dan memprint error nya
                 * ke LogCat
                 */
                println(databaseError.details + " " + databaseError.message)
            }
        })
        Log.e(ContentValues.TAG,daftarinfo.toString())
        addMarker(daftarinfo)

    }

    private fun addMarker(listData: List<lokasi>){
        var lastStoryLatLng = LatLng(0.0,0.0)
        val marker = LatLng(-6.302446, 107.097498)
        Log.e("List Data lokasi",listData.toString())

        for(i in listData){
            lastStoryLatLng = LatLng(i.latitude.toDouble(),i.longitude.toDouble())
            mMap.addMarker(
                MarkerOptions()
                    .position(lastStoryLatLng)
                    .title("Subject Test")
                    .snippet("test")
                    .icon(vectorToBitmap(R.drawable.ic_baseline_gps_fixed_24, Color.parseColor("#EE4B2B")))
            )

        }
        lastStoryLatLng = marker
        mMap.addMarker(
            MarkerOptions()
                .position(marker)
                .title("Subject Test")
                .snippet("test")
                .icon(vectorToBitmap(R.drawable.ic_baseline_gps_fixed_24, Color.parseColor("#EE4B2B")))
        )

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastStoryLatLng, 15f))

    }

    private fun vectorToBitmap(@DrawableRes id: Int, @ColorInt color: Int): BitmapDescriptor {
        val vectorDrawable = ResourcesCompat.getDrawable(resources, id, null)
        if (vectorDrawable == null) {
            Log.e("BitmapHelper", "Resource not found")
            return BitmapDescriptorFactory.defaultMarker()
        }
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        DrawableCompat.setTint(vectorDrawable, color)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }


}

