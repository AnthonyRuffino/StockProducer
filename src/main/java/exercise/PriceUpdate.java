package exercise;

public class PriceUpdate {
	
	private final String companyName;
	private final double price;
	
	public PriceUpdate(String companyName, double price) {
		this.companyName = companyName;
		this.price = price;
	}
	
	public String getCompanyName() {
		return this.companyName;
	}
	
	public double getPrice() {
		return this.price;
	}
	
	@Override
	public String toString() {
		return companyName + " - " + price;
	}

	/**
	 * This hashCode to method is only considers the companyName because we
	 * need to determine if price updates are from the same company.  We are indifferent to the actual price value
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((companyName == null) ? 0 : companyName.hashCode());
		return result;
	}

	/**
	 * This equals method is only considers the companyName because we
	 * need to determine if price updates are from the same company.  We are indifferent to the actual price value
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PriceUpdate other = (PriceUpdate) obj;
		if (companyName == null) {
			if (other.companyName != null)
				return false;
		} else if (!companyName.equals(other.companyName))
			return false;
		return true;
	}
	
	
}
