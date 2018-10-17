package com.apap.tugas1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.model.ProvinsiModel;
import com.apap.tugas1.service.JabatanService;

@Controller
public class JabatanController {
	
	@Autowired
	private JabatanService jabService;
	
	
	@RequestMapping(value = "/jabatan/view", method = RequestMethod.GET)
	private String findPegawai(@RequestParam(value = "idJabatan") Long idJabatan, Model model) {
		JabatanModel jabatan = jabService.getJabatanDetailById(idJabatan).get();
		if (jabatan!=null ) {
			model.addAttribute("jabatan", jabatan);
			model.addAttribute("title", "Detail Jabatan");
			return "JabatanDetail";
		}
		return "error";
		
	}

	
	@RequestMapping(value = "/jabatan/tambah")
	private String tambahJabatan(Model model) {
		JabatanModel jab = new JabatanModel();
		model.addAttribute("jabatan",jab);
		return "TambahJabatan";
	}
	
	@RequestMapping(value = "/jabatan/tambah/sukses",method = RequestMethod.POST)
	private String tambahJabatanSubmit(@ModelAttribute JabatanModel jabatan, Model model) {
		jabService.addJabatan(jabatan);
		model.addAttribute("msg","Jabatan berhasil ditambahkan");
		return "add";
	}
	
	@RequestMapping(value = "/jabatan/ubah", method = RequestMethod.GET)
	private String updateJabatan(@RequestParam(value = "idJabatan") Long idJabatan, Model model) {
		JabatanModel old = jabService.getJabatanDetailById(idJabatan).get();
		JabatanModel jabatan = new JabatanModel();
		jabatan.setId(old.getId());
		model.addAttribute("jabatan",jabatan);
		model.addAttribute("oldJab", old);
		return "UpdateJabatan.html";
		
	}
	
	@RequestMapping(value = "/jabatan/ubah/sukses",method = RequestMethod.POST)
	private String updateJabatanSubmit(@ModelAttribute JabatanModel jabatan, Model model) {
		JabatanModel real = jabService.getJabatanDetailById(jabatan.getId()).get();
		real.setNama(jabatan.getNama());
		real.setDeskripsi(jabatan.getDeskripsi());
		real.setGajiPokok(jabatan.getGajiPokok());
		jabService.updateJabatan(real);
		model.addAttribute("msg","Jabatan berhasil diubah");
		return "add";
	}
	
	@RequestMapping(value = "/jabatan/hapus",method = RequestMethod.POST)
	private String hapusJabatan(@ModelAttribute JabatanModel jabatan, Model model) {
		System.out.println(jabatan.getId());
		JabatanModel real = jabService.getJabatanDetailById(jabatan.getId()).get();
		if (real.getPegawaiList().isEmpty()) {
			jabService.deleteJabatan(real);
			model.addAttribute("msg", "Jabatan berhasil dihapus");
		}
		else {
			model.addAttribute("msg", "Jabatan tidak berhasil dihapus karena ada pegawai yang memiliki jabatan ini");
		}
		return "add";
	}
}
