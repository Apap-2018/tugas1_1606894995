package com.apap.tugas1.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
	
	
	@RequestMapping(value = "/pegawai/cari",method = RequestMethod.GET)
	private  String filter(@RequestParam(value = "idProvinsi", required=false) Optional<String> idProvinsi,
			@RequestParam(value="idInstansi",  required=false) Optional<String> id_instansi,
			@RequestParam(value="idJabatan", required=false) Optional<String> id_jabatan,
			Model model) {
		List<JabatanModel> allJabatan = jabService.findAllJabatan();
		List<InstansiModel> allInstansi = instansiService.findAllInstansi();
		List<ProvinsiModel> allProvinsi = provService.findAllProvinsi();
		model.addAttribute("allInstansi",allInstansi);
		model.addAttribute("allProvinsi",allProvinsi);
		model.addAttribute("allJabatan",allJabatan);
		model.addAttribute("title", "Cari Pegawai");
		
		List<PegawaiModel> allPegawai = pegawaiService.findAllPegawai();
		List<PegawaiModel> result = new ArrayList<>();
		if (idProvinsi.isPresent()) {
			System.out.println("is it even enter this");
			if (id_instansi.isPresent() && id_jabatan.isPresent()) {
				// ALL instansi, jabatan, provinsi
				//pke instansi per provinsi aja
				System.out.println("masuk id instansi and jabatan");
				List<PegawaiModel> temp = new ArrayList<>();
				Long idInstansi = Long.parseLong(id_instansi.get());
				InstansiModel instansi = instansiService.getInstansiDetailById(idInstansi).get();
				System.out.println(instansi.getNama());
				Long idJabatan = Long.parseLong(id_jabatan.get());
				JabatanModel jabatan = jabService.getJabatanDetailById(idJabatan).get();
				System.out.println(jabatan.getNama());
				temp = pegawaiService.getPegawaiByInstansi(instansi);
				System.out.println(temp.size());
				for (PegawaiModel peg : temp) {
					for (JabatanModel jab : peg.getJabatanList()) {
						if (jab.equals(jabatan)) {
							result.add(peg);
						}
					}
				}
				System.out.println(result.size());
			}
			else if (!(id_instansi.isPresent()) && id_jabatan.isPresent()) {
				//provinsi
				//jabatan
				//provinsi & jabatan
				List<PegawaiModel> temp = new ArrayList<>();
				Long idProv = Long.parseLong(idProvinsi.get());
				ProvinsiModel prov = provService.getProvinsiDetailById(idProv).get();
				for (PegawaiModel peg : allPegawai) {
					if (peg.getInstansi().getProvinsi().equals(prov)) {
						temp.add(peg);
					}
				}
				Long idJabatan = Long.parseLong(id_jabatan.get());
				JabatanModel jabatan = jabService.getJabatanDetailById(idJabatan).get();
				for (PegawaiModel peg : temp) {
					for (JabatanModel jab : peg.getJabatanList()) {
						if (jab.equals(jabatan)) {
							result.add(peg);
						}
					}
				}
			}
			else if(id_instansi.isPresent() && !(id_jabatan.isPresent())) { 
				//provinsi dan instansi
				System.out.println("provinsi dan instansi");
				Long idInstansi = Long.parseLong(id_instansi.get());
				InstansiModel instansi = instansiService.getInstansiDetailById(idInstansi).get();
				result = pegawaiService.getPegawaiByInstansi(instansi);
				
			}
			else if(!(id_instansi.isPresent()) && !(id_jabatan.isPresent())) {
				//just provinsi
				Long idProv = Long.parseLong(idProvinsi.get());
				ProvinsiModel prov = provService.getProvinsiDetailById(idProv).get();
				for (PegawaiModel peg : allPegawai) {
					if(peg.getInstansi().getProvinsi().equals(prov)) {
						result.add(peg);
					}
				}
			}
		}
		else {
			//jabatan
			//instansi
			//jabatan dan instansi-worked
			if (id_jabatan.isPresent() && id_instansi.isPresent()) {
				List<PegawaiModel> temp = new ArrayList<>();
				Long idInstansi = Long.parseLong(id_instansi.get());
				InstansiModel instansi = instansiService.getInstansiDetailById(idInstansi).get();
				Long idJabatan = Long.parseLong(id_jabatan.get());
				JabatanModel jabatan = jabService.getJabatanDetailById(idJabatan).get();
				temp = pegawaiService.getPegawaiByInstansi(instansi);
				for (PegawaiModel peg : temp) {
					for (JabatanModel jab : peg.getJabatanList()) {
						if (jab.equals(jabatan)) {
							result.add(peg);
						}
					}
				}
			}
			
			//jabatan doang
			else if(id_jabatan.isPresent() && !(id_instansi.isPresent())) {
				Long idJabatan = Long.parseLong(id_jabatan.get());
				JabatanModel jabatan = jabService.getJabatanDetailById(idJabatan).get();
				for (PegawaiModel peg : allPegawai) {
					for (JabatanModel jab : peg.getJabatanList()) {
						if (jab.equals(jabatan)) {
							result.add(peg);
						}
					}
				}
			}
			//instansi doang
			else if(!(id_jabatan.isPresent()) && id_instansi.isPresent()) {
				Long idInstansi = Long.parseLong(id_instansi.get());
				InstansiModel instansi = instansiService.getInstansiDetailById(idInstansi).get();
				result = pegawaiService.getPegawaiByInstansi(instansi);
			}
			else if(!(id_jabatan.isPresent()) && !(id_instansi.isPresent())) {
				result = null;
			}
		}
		model.addAttribute("allData",result);
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
		model.addAttribute("title", "Tambah Pegawai");
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
		model.addAttribute("title", "Lihat Pegawai Tertua & Termuda");
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
		model.addAttribute("title", "Tambah Pegawai");
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
		String nipPegawai = pegawaiService.generateNip(pegawai);
		System.out.println(nipPegawai);
		pegawai.setNip(nipPegawai);
		pegawaiService.addPegawai(pegawai);
		String msg = "Pegawai dengan NIP "+nipPegawai+" berhasil ditambahkan";
		model.addAttribute("msg",msg);
		model.addAttribute("title", "Sukses");
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
		model.addAttribute("title", "Ubah Data Pegawai");
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
		model.addAttribute("title", "Ubah Data Pegawai");
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
		model.addAttribute("title", "Sukses");
		return "add";
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
