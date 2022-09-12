package com.total.eyecare.ui.camera

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.total.eyecare.adapter.CameraAdapter
import com.total.eyecare.classes.camera
import com.total.eyecare.databinding.FragmentCameraBinding
import com.total.eyecare.ui.addLokasi.AddLokasiActivity

class CameraFragment : Fragment() {
    private var database: DatabaseReference? = null

    private var _binding: FragmentCameraBinding? = null
    private lateinit var cameraAdapter: CameraAdapter
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCameraBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCameraList()
        val addCamera = binding.addCamera
        addCamera.setOnClickListener{
            startActivity(Intent(activity,AddLokasiActivity::class.java))
        }

    }
    private fun showData(list: List<camera>) {

        Log.e("showData",list.toString())
        val layoutManager = LinearLayoutManager(activity)
        val recyclerview = binding.rvCamera
        recyclerview.layoutManager = layoutManager
        val adapter = CameraAdapter(list)
        adapter.setOnItemClickCallback(object : CameraAdapter.OnItemClickCallback {
            override fun onItemClicked(data: camera) {
                val intentDetail = Intent(activity, DetailCameraActivity::class.java)
                intentDetail.putExtra("ip", data.ip)
                startActivity(intentDetail)
            }
        })
        recyclerview.adapter = adapter
    }
    private fun getCameraList(): List<camera>{
        var daftarinfo = listOf<camera>()
        database = FirebaseDatabase.getInstance().reference

        database?.child("camera")?.addValueEventListener(object : ValueEventListener {
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
                    val info: camera? = noteDataSnapshot.getValue(camera::class.java)
                    /**
                     * Menambahkan object Barang yang sudah dimapping
                     * ke dalam ArrayList
                     */

                    if (info != null) {
                        daftarinfo += (info)

                    }
                }
                showData(daftarinfo)
                Log.e("on data change",daftarinfo.toString())
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
        return daftarinfo
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}