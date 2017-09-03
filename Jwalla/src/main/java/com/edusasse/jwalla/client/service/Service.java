package com.edusasse.jwalla.client.service;

public class Service implements i_Service {
	// Titulo do servico
	private String name;
	// Descricao
	private String desc;
	// Versao
	private String version;
	// Endereco
	private  String address;
	// Porta
	private int door;

	public Service(String name, String desc, String version, String address, int door) {
		this.name = name;
		this.desc = desc;
		this.version = version;
		this.address = address;
		this.door = door;
	}

	public Service() {

	}

	/* (non-Javadoc)
	 * @see com.edusasse.jwalla.client.service.i_Service#setDoor(int)
	 */
	public void setDoor(int door) {
		this.door = door;
	}

	/* (non-Javadoc)
	 * @see com.edusasse.jwalla.client.service.i_Service#getDoor()
	 */
	public int getDoor() {
		return door;
	}

	/* (non-Javadoc)
	 * @see com.edusasse.jwalla.client.service.i_Service#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see com.edusasse.jwalla.client.service.i_Service#getDesc()
	 */
	public String getDesc() {
		return desc;
	}

	/* (non-Javadoc)
	 * @see com.edusasse.jwalla.client.service.i_Service#setDesc(java.lang.String)
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/* (non-Javadoc)
	 * @see com.edusasse.jwalla.client.service.i_Service#getVersion()
	 */
	public String getVersion() {
		return version;
	}

	/* (non-Javadoc)
	 * @see com.edusasse.jwalla.client.service.i_Service#setVersion(java.lang.String)
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/* (non-Javadoc)
	 * @see com.edusasse.jwalla.client.service.i_Service#getName()
	 */
	public String getName() {
		return name;
	}

	@Override
	public String getAddress() {
		return this.address;
	}

	@Override
	public void setAddress(String ad) {
		this.address = ad;
		
	}

}
