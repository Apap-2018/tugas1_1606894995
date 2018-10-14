package com.apap.tugas1.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;



@Entity
@Table (name="instansi")
public class InstansiModel implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	@Size(max = 255)
	@Column(name = "nama_jabatan", nullable = false, unique = true)
	private String nama_jabatan;
	
	@NotNull
	@Size(max = 255)
	@Column(name = "deskripsi_jabatan", nullable = false)
	private String deskripsi_jabatan;
	
	@NotNull
	@Column(name = "gaji_pokok", nullable = false)
	private double gaji_pokok;
	
	@OneToMany(mappedBy = "instansi", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<PegawaiModel> listPegawai;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "provinsi_id", referencedColumnName = "id", nullable = false)
	@OnDelete(action = OnDeleteAction.NO_ACTION)
	@JsonIgnore
	private ProvinsiModel provinsi;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNama_jabatan() {
		return nama_jabatan;
	}

	public void setNama_jabatan(String nama_jabatan) {
		this.nama_jabatan = nama_jabatan;
	}

	public String getDeskripsi_jabatan() {
		return deskripsi_jabatan;
	}

	public void setDeskripsi_jabatan(String deskripsi_jabatan) {
		this.deskripsi_jabatan = deskripsi_jabatan;
	}

	public double getGaji_pokok() {
		return gaji_pokok;
	}

	public void setGaji_pokok(double gaji_pokok) {
		this.gaji_pokok = gaji_pokok;
	}

	public List<PegawaiModel> getListPegawai() {
		return listPegawai;
	}

	public void setListPegawai(List<PegawaiModel> listPegawai) {
		this.listPegawai = listPegawai;
	}

	public ProvinsiModel getProvinsi() {
		return provinsi;
	}

	public void setProvinsi(ProvinsiModel provinsi) {
		this.provinsi = provinsi;
	}

	
}
