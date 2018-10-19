package com.apap.tugas1.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.model.ProvinsiModel;
import com.apap.tugas1.service.InstansiService;
import com.apap.tugas1.service.JabatanService;
import com.apap.tugas1.service.PegawaiService;
import com.apap.tugas1.service.ProvinsiService;

@Controller
public class PegawaiController {
	@Autowired
	private PegawaiService pegawaiService;
	
	@Autowired
	private ProvinsiService provService;
	
	@Autowired
	private InstansiService instansiService;
	
	@Autowired
	private JabatanService jabService;
	
	@RequestMapping("/")
	private String home(Model model) {
		List<JabatanModel> allJabatan = jabService.findAllJabatan();
		List<InstansiModel> allInstansi = instansiService.findAllInstansi();
		model.addAttribute("allInstansi",allInstansi);
		model.addAttribute("allJabatan",allJabatan);
		model.addAttribute("title", "Home");
		return "HomePage";
	}
	@RequestMapping("/pegawai/cari")
	private String cari(Model model) {
		List<JabatanModel> allJabatan = jabService.findAllJabatan();
		List<InstansiModel> allInstansi = instansiService.findAllInstansi();
		List<ProvinsiModel> allProvinsi = provService.findAllProvinsi();
		model.addAttribute("allInstansi",allInstansi);
		model.addAttribute("allProvinsi",allProvinsi);
		model.addAttribute("allJabatan",allJabatan);
		model.addAttribute("title", "Home");
		return "CariPegawai";
	}
	@RequestMapping(value="/pegawai/tambah",method = RequestMethod.POST, params= {"addRow"})
	private String addRow (@ModelAttribute PegawaiModel pegawai, Model model, BindingResult bindingResult) {
		if (pegawai.getJabatanList() == null) {
			pegawai.setJabatanList(new ArrayList());
		}
		System.out.println(pegawai.getJabatanList().size());
		pegawai.getJabatanList().add(new JabatanModel());
		
		List<JabatanModel> jab = jabService.findAllJabatan();
		List<ProvinsiModel> prov = provService.findAllProvinsi();
		model.addAttribute("listOfProvinsi", prov);
		model.addAttribute("pegawai", pegawai);
		model.addAttribute("jabatanList",jab);
		return "TambahPegawai";
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
	@RequestMapping(value = "/pegawai/termuda-tertua", method = RequestMethod.GET)
	private String tuaMuda(@RequestParam(value = "idInstansi") Long idInstansi, Model model) {
		List<PegawaiModel> all = pegawaiService.findAllPegawai();
		InstansiModel instansi = instansiService.getInstansiDetailById(idInstansi).get();
		ArrayList<PegawaiModel> baru = new ArrayList<>();
		for (PegawaiModel pegawai : all) {
			if (pegawai.getInstansi().equals(instansi)) {
				baru.add(pegawai);
			}
		}
		PegawaiModel termuda = Collections.min(baru,comp);
		PegawaiModel tertua = Collections.max(baru,comp);
		model.addAttribute("termuda", termuda);
		model.addAttribute("tertua", tertua);
		return "TertuaTermuda";
	}
	@RequestMapping(value = "/pegawai/tambah")
	private String tambahPegawai(Model model) {
		PegawaiModel peg = new PegawaiModel();
		if (peg.getJabatanList()==null) {
			peg.setJabatanList(new ArrayList());
		}
		peg.getJabatanList().add(new JabatanModel());
		List<ProvinsiModel> prov = provService.findAllProvinsi();
		List<JabatanModel> jab = jabService.findAllJabatan();
		model.addAttribute("jabatanList",jab);
		model.addAttribute("pegawai", peg);
		model.addAttribute("listOfProvinsi", prov);
		return "TambahPegawai";
	}
	
	@RequestMapping(value = "/pegawai/tambah/instansi",method = RequestMethod.GET)
	private @ResponseBody List<InstansiModel> cekInstansi(@RequestParam(value="provinsiId") long provinsiId) {
		ProvinsiModel getProv = provService.getProvinsiDetailById(provinsiId).get();
		
		return getProv.getInstansiList();
	}
	
	
	
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.POST, params= {"submit"})
	private String tambahPegawaiSubmit(@ModelAttribute PegawaiModel pegawai, Model model) {
		System.out.println(pegawai.getNama());
		System.out.println(pegawai.getTahunMasuk());
		System.out.println(pegawai.getTempatLahir());
		System.out.println(pegawai.getTanggalLahir());
		System.out.println(pegawai.getInstansi().getNama());
		System.out.println("total jabatanku->"+pegawai.getJabatanList().size());
		String nipPegawai = generateNip(pegawai);
		System.out.println(nipPegawai);
		pegawai.setNip(nipPegawai);
		pegawaiService.addPegawai(pegawai);
		String msg = "Pegawai dengan NIP "+nipPegawai+" berhasil ditambahkan";
		model.addAttribute("msg",msg);
		return "add";
	}
	
	@RequestMapping(value = "/pegawai/ubah")
	private String ubahPegawai(@RequestParam(value="nip") String nip, Model model) {
		PegawaiModel real = pegawaiService.getPegawaiByNip(nip);
		
		List<ProvinsiModel> prov = provService.findAllProvinsi();
		List<JabatanModel> jab = jabService.findAllJabatan();
		model.addAttribute("jabatanList",jab);
		model.addAttribute("pegawai", real);
		model.addAttribute("listOfProvinsi", prov);
		return "UbahPegawai";
	}
	@RequestMapping(value="/pegawai/ubah",method = RequestMethod.POST, params= {"addRow"})
	private String addRowUpdate (@ModelAttribute PegawaiModel pegawai, Model model, BindingResult bindingResult) {
		if (pegawai.getJabatanList() == null) {
			pegawai.setJabatanList(new ArrayList());
		}
		System.out.println(pegawai.getJabatanList().size());
		pegawai.getJabatanList().add(new JabatanModel());
		
		List<JabatanModel> jab = jabService.findAllJabatan();
		List<ProvinsiModel> prov = provService.findAllProvinsi();
		model.addAttribute("listOfProvinsi", prov);
		model.addAttribute("pegawai", pegawai);
		model.addAttribute("jabatanList",jab);
		return "UbahPegawai";
	}
	@RequestMapping(value = "/pegawai/ubah", method = RequestMethod.POST, params= {"submit"})
	private String updatePegawaiSubmit(@ModelAttribute PegawaiModel pegawai, Model model) {
		PegawaiModel real = pegawaiService.getPegawaiDetailById(pegawai.getId()).get();
		real.setNama(pegawai.getNama());
		real.setJabatanList(pegawai.getJabatanList());
		real.setTahunMasuk(pegawai.getTahunMasuk());
		real.setTanggalLahir(pegawai.getTanggalLahir());
		real.setTempatLahir(pegawai.getTempatLahir());
		pegawaiService.addPegawai(real);
		String msg = "Pegawai dengan NIP "+real.getNip()+" berhasil diubah";
		model.addAttribute("msg",msg);
		return "add";
	}
	
	private String generateNip(PegawaiModel pegawai) {
		DateFormat df = new SimpleDateFormat("ddMMYY");
		Date tglLahir = pegawai.getTanggalLahir();
		String formatted = df.format(tglLahir);
		System.out.println("date->"+formatted);
		
		Long kodeInstansi = pegawai.getInstansi().getId();
		System.out.println("kode instansi->"+kodeInstansi);
		
		int idAkhir = 0;
		for (PegawaiModel peg : pegawaiService.findAllPegawai()) {
			if (peg.getTanggalLahir().equals(pegawai.getTanggalLahir()) && peg.getTahunMasuk().equals(pegawai.getTahunMasuk())) {
				idAkhir+=1;
			}
		}
		idAkhir+=1;
		
		String kodeMasuk = "";
		if (idAkhir<10) {
			kodeMasuk = "0"+idAkhir;
		}
		else {
			kodeMasuk = Integer.toString(idAkhir);
		}
		
		System.out.println(kodeInstansi+formatted+pegawai.getTahunMasuk()+kodeMasuk);
		return kodeInstansi+formatted+pegawai.getTahunMasuk()+kodeMasuk;
		
	}
	public static Comparator<PegawaiModel> comp = new Comparator<PegawaiModel>() {

		@Override
		public int compare(PegawaiModel o1, PegawaiModel o2) {
			if (o1.calculateUmur()<o2.calculateUmur()) {
				return -1;
			}
			else if (o1.calculateUmur()>o2.calculateUmur()) {
				return 1;
			}
			return 0;
		}
	};
}
