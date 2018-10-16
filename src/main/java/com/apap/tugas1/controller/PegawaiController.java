package com.apap.tugas1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.model.ProvinsiModel;
import com.apap.tugas1.service.PegawaiService;
import com.apap.tugas1.service.ProvinsiService;

@Controller
public class PegawaiController {
	@Autowired
	private PegawaiService pegawaiService;
	
	@Autowired
	private ProvinsiService provService;
	
	@RequestMapping("/")
	private String home(Model model) {
		model.addAttribute("title", "Home");
		return "HomePage";
	}
	
	@RequestMapping(value = "/pegawai", method = RequestMethod.GET)
	private String findPegawai(@RequestParam(value = "nip") String nip, Model model) {
		PegawaiModel pegawai = pegawaiService.getPegawaiByNip(nip);
		if (pegawai!=null ) {
			model.addAttribute("pegawai", pegawai);
			model.addAttribute("title", "Detail Pegawai");
			return "PegawaiDetailPage";
		}
		return "error";
		
		
	}
	@RequestMapping(value = "/pegawai/tambah")
	private String tambahPegawai(Model model) {
		PegawaiModel peg = new PegawaiModel();
		List<ProvinsiModel> prov = provService.findAllProvinsi();
		model.addAttribute("pegawai", peg);
		model.addAttribute("listOfProvinsi", prov);
		return "TambahPegawai";
	}
	
	@RequestMapping(value = "/pegawai/tambah")
	private String cekInstansi(Model model) {
		PegawaiModel peg = new PegawaiModel();
		List<ProvinsiModel> prov = provService.findAllProvinsi();
		model.addAttribute("pegawai", peg);
		model.addAttribute("listOfProvinsi", prov);
		return "TambahPegawai";
	}
}
