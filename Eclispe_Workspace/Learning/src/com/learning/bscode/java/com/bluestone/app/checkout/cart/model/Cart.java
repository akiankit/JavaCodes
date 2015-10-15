package com.bluestone.app.checkout.cart.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.envers.Audited;

import com.bluestone.app.account.model.Customer;
import com.bluestone.app.account.model.Visitor;
import com.bluestone.app.core.model.BaseEntity;
import com.bluestone.app.core.util.NumberUtil;
import com.bluestone.app.design.model.product.Product;
import com.bluestone.app.design.model.product.Product.PRODUCT_TYPE;
import com.bluestone.app.payment.spi.model.MasterPayment;
import com.bluestone.app.shipping.model.Address;
import com.bluestone.app.voucher.model.DiscountVoucher;

/**
 * @author admin
 *
 */
@Audited
@Entity
@Table(name = "cart")
public class Cart extends BaseEntity implements Cloneable {
	
	private static final long serialVersionUID = -4461143736086272397L;

	@OneToMany(mappedBy = "cart", cascade=CascadeType.ALL, orphanRemoval=true)
	@LazyCollection(LazyCollectionOption.FALSE)
	@BatchSize(size=10)
	private List<CartItem> cartItems = new ArrayList<CartItem>();

    @ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "customer_id", nullable = true)
    private Customer customer;
    
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "visitor_id", nullable = true)
    private Visitor visitor;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "shipping_address_id", nullable = true)
    private Address shippingAddress;
    
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "billing_address_id", nullable = true)
    private Address billingAddress;
    
    @ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="discountvoucher_id",nullable=true)
    private DiscountVoucher discountVoucher;

    @Column(scale=2)
    private BigDecimal discountPrice = new BigDecimal(0);
    
    @OneToOne(mappedBy = "cart", fetch=FetchType.EAGER)
    private MasterPayment masterPayment;
    
    public Visitor getVisitor() {
        return visitor;
    }

    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;
    }

    
    /**
     * Use getActiveCartItems(), to get the list of active cart items. This method is only meant for hibernate to populate cartItems
     * @return
     */
    @Deprecated
    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }
	
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public void setShippingAddress(Address address) {
        this.shippingAddress = address;
    }
    
    public Address getShippingAddress() {
        return shippingAddress;
    }
    
    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

	public DiscountVoucher getDiscountVoucher() {
		return discountVoucher;
	}

	public void setDiscountVoucher(DiscountVoucher discountVoucher) {
		this.discountVoucher = discountVoucher;
	}

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    public BigDecimal getDiscountPrice() {
        return NumberUtil.roundUp(discountPrice);
    }
    
    public BigDecimal getFinalPrice() {
        return getTotalPrice().subtract(discountPrice);
    }

    public MasterPayment getMasterPayment() {
        return masterPayment;
    }

    public void setMasterPayment(MasterPayment masterPayment) {
        this.masterPayment = masterPayment;
    }


    public void addCartItem(CartItem cartItem) {
        if(this.cartItems == null) {
            this.cartItems = new ArrayList<CartItem>();
        }
        this.cartItems.add(cartItem);
    }
    
    public List<CartItem> getCartItems(PRODUCT_TYPE product_type) {
        List<CartItem> cartItemList = new LinkedList<CartItem>();
        for (CartItem cartItem : getActiveCartItems()) {
            if (cartItem.getProduct().getProductType().equals(product_type)) {
                cartItemList.add(cartItem);
            }
        }
        return cartItemList;
    }

    public CartItem getCartItem(Long cartItemId) {
        for (CartItem cartItem : getActiveCartItems()) {
            if(cartItemId == cartItem.getId()) {
                return cartItem;
            }
        }
        return null;
    }
    
    public CartItem getCartItem(String sKuCodeWithSize, CartItem parentCartItem) {
        List<CartItem> items = getActiveCartItems();
        if(items == null) {
            return null;
        }
        for (CartItem eachCartItem : items) {
            CartItem eachCartParentCartItem = eachCartItem.getParentCartItem();
            String productSkuCodeWithSize = eachCartItem.getSkuCodeWithSize();
            if(eachCartParentCartItem != null){
                if(productSkuCodeWithSize.equals(sKuCodeWithSize) && eachCartParentCartItem.equals(parentCartItem)) {
                   return eachCartItem;
                }
            } else {
                if(productSkuCodeWithSize.equals(sKuCodeWithSize)) {
                    return eachCartItem;
                }
            }
        }
        return null;
    }

    public BigDecimal getTotalPrice() {
        BigDecimal total = new BigDecimal(0);
        List<CartItem> items = getActiveCartItems();
        for (CartItem eachItem : items) {
            total = total.add(eachItem.getTotalPrice());
        }
        return total;
    }
    
    // only return list of items which are not deleted
    public List<CartItem> getActiveCartItems() {
        // if cart is not active , that means order is placed ,return all the cart items
        if(!isActive()) {
            return cartItems;
        } else {
            // order hasn't been placed for this cart. return only cart items which have active products
            List<CartItem> activeCartItems = new ArrayList<CartItem>();
            for (CartItem cartItem : cartItems) {
                Product product = cartItem.getProduct();
                if(product.isProductActive() && cartItem.isActive()) {
                    activeCartItems.add(cartItem);
                }
            }
            return activeCartItems;
        }
    }
    
    // only return list of items which products are active and have no parent
    public List<CartItem> getActiveParentCartItems() {
        List<CartItem> activeParentCartItems = new ArrayList<CartItem>();
        for (CartItem eachActiveCartItem : getActiveCartItems()) {
            if(eachActiveCartItem.getParentCartItem() == null) {
                activeParentCartItems.add(eachActiveCartItem);
            }
        }
        return activeParentCartItems;
    }
    
    public boolean isGoldMineCart(){
    	//Checks if cart is for gold mine product. 
    	boolean isGoldMineCart = false;
    	List<CartItem> activeCartItems = getActiveCartItems();
    	for (CartItem cartItem : activeCartItems) {
			if(cartItem.getProduct().getCategory().equals(PRODUCT_TYPE.GOLDMINE.toString())){
				isGoldMineCart = true;
				break;
			}
		}
    	return isGoldMineCart;
    }

    
    public int getItemCount(){
        return getActiveCartItems().size();
    }
    
    
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        //sb.append(super.toString());
        sb.append("Cart : id=").append(getId());
        sb.append("{\n\tCartItems=").append(getActiveCartItems()).append("\n");
        sb.append("\t").append(customer).append("\n");
        sb.append("\t DiscountPrice=").append(discountPrice).append("\n");
        sb.append("\t MasterPayment=").append(masterPayment);
        sb.append("\t").append(visitor).append("\n");
        sb.append("}");
        return sb.toString();
    }



}
