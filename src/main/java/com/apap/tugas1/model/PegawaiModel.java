package com.apap.tugas1.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "pegawai")
public class PegawaiModel implements Serializable, Comparable<PegawaiModel>{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	@Size(max = 255)
	@Column(name = "nip", nullable = false, unique = true)
	private String nip;
	
	@NotNull
	@Size(max = 255)
	@Column(name = "nama", nullable = false)
	private String nama;
	
	@NotNull
	@Size(max = 255)
	@Column(name = "tempat_lahir", nullable = false)
	private String tempatLahir;
	
	@NotNull
	@Column(name = "tanggal_lahir")
	private Date tanggalLahir;
	
	@NotNull
	@Size(max = 255)
	@Column(name = "tahun_masuk", nullable = false)
	private String tahunMasuk;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_instansi", referencedColumnName = "id", nullable = false)
	@OnDelete(action = OnDeleteAction.NO_ACTION)
	@JsonIgnore
	private InstansiModel instansi;
	
	@ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                CascadeType.PERSIST,
                CascadeType.MERGE
            })
    @JoinTable(name = "jabatan_pegawai",
            joinColumns = { @JoinColumn(name = "id_pegawai") },
            inverseJoinColumns = { @JoinColumn(name = "id_jabatan") })
    private List<JabatanModel> jabatanList;

	public double getGaji() {
		double gajiUtama = 0;
		double tunjangan = this.instansi.getProvinsi().getPresentaseTunjangan();
		System.out.println(tunjangan);
		if (jabatanList.size()>1) {
			JabatanModel max = Collections.max(jabatanList, pegComp);
			System.out.println(max.getGajiPokok());
			System.out.println(jabatanList.get(0).getGajiPokok());
			System.out.println(jabatanList.get(1).getGajiPokok());
			gajiUtama = max.getGajiPokok() + ((tunjangan / 100) * max.getGajiPokok());
		}
		else {
			gajiUtama = jabatanList.get(0).getGajiPokok() + (tunjangan * jabatanList.get(0).getGajiPokok());
		}
		return gajiUtama;
		
		
	}
	
	private static Comparator<JabatanModel> pegComp = new Comparator<JabatanModel>() {

		@Override
		public int compare(JabatanModel o1, JabatanModel o2) {
			if (o1.getGajiPokok()<o2.getGajiPokok()) {
				return -1;
			}
			else if (o1.getGajiPokok()>o2.getGajiPokok()) {
				return 1;
			}
			return 0;
		}
	};
	public int calculateUmur() {
		  Date now = new Date();
		  DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		  int d1 = Integer.parseInt(formatter.format(tanggalLahir));
		  int d2 = Integer.parseInt(formatter.format(now));
		  int age = (d2 - d1) / 10000;   
		  return age;
	}
	public List<JabatanModel> getJabatanList() {
		return jabatanList;
	}

	public void setJabatanList(List<JabatanModel> jabatanList) {
		this.jabatanList = jabatanList;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNip() {
		return nip;
	}

	public void setNip(String nip) {
		this.nip = nip;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getTempatLahir() {
		return tempatLahir;
	}

	public void setTempatLahir(String tempatLahir) {
		this.tempatLahir = tempatLahir;
	}

	public Date getTanggalLahir() {
		return tanggalLahir;
	}

	public void setTanggalLahir(Date tanggalLahir) {
		this.tanggalLahir = tanggalLahir;
	}

	public String getTahunMasuk() {
		return tahunMasuk;
	}

	public void setTahunMasuk(String tahunMasuk) {
		this.tahunMasuk = tahunMasuk;
	}

	public InstansiModel getInstansi() {
		return instansi;
	}

	public void setInstansi(InstansiModel instansi) {
		this.instansi = instansi;
	}
	@Override
	public int compareTo(PegawaiModel o) {
		int otherPegawai = o.calculateUmur();
		return this.calculateUmur() - otherPegawai;
	}
	
}