package com.total.eyecare.ui.dashboard

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.*
import com.total.eyecare.classes.BriefInformation
import com.total.eyecare.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var database: DatabaseReference? = null
    private val daftarinfo:MutableList<BriefInformation> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val homeViewModel =
//            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        getData()
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            binding.tvCabang.text = it.alamat
//            binding.tvPerusahaan.text = it.perusahaan
//            binding.tvJumlahPegawai.text = it.telepon
//        }
        return root
    }

    private fun getData(){
        database = FirebaseDatabase.getInstance().reference
        database?.child("informasi")?.addValueEventListener(object : ValueEventListener {
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
                    val info: BriefInformation? = noteDataSnapshot.getValue(BriefInformation::class.java)
                    /**
                     * Menambahkan object Barang yang sudah dimapping
                     * ke dalam ArrayList
                     */

                    if (info != null) {
                        daftarinfo.add(info)
                        binding.tvCabang.text = info.alamat
                        binding.tvPerusahaan.text = info.perusahaan
                        binding.tvJumlahPegawai.text = info.telepon
                    }
                }
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
        for(i in daftarinfo){
            binding.tvCabang.text = i.alamat
            binding.tvPerusahaan.text = i.perusahaan
            binding.tvJumlahPegawai.text = i.telepon
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}