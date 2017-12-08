package com.bluestone.app.checkout.cart.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.envers.Audited;

import com.bluestone.app.core.model.BaseEntity;
import com.bluestone.app.design.model.product.Product;

@Audited
@Entity
@Table(name = "cartitem")
public class CartItem extends BaseEntity implements Comparable<CartItem>,Cloneable {
    
    private static final long                     serialVersionUID = 4134652647253355430L;
    
    private String                                sKuCodeWithSize;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product                               product;
    
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_cart_item_id", nullable = true)
    private CartItem  parentCartItem;

    @OneToMany(mappedBy = "parentCartItem", cascade=CascadeType.REMOVE, fetch = FetchType.EAGER, orphanRemoval=true)
    @BatchSize(size=10)
    private Set<CartItem>    childCartItems = new HashSet<CartItem>();

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart                                  cart;
    
    private int                                   quantity;
    
    private String                                size;
    
    public CartItem() {
    }
    
    public CartItem(Product product, String size, String sKuCodeWithSize) {
        this.product = product;
        this.size = size;
        this.sKuCodeWithSize = sKuCodeWithSize;
    }
    
    public Cart getCart() {
        return cart;
    }
    
    public void setCart(Cart cart) {
        this.cart = cart;
        cart.addCartItem(this);
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public String getSize() {
        return size;
    }
    
    public void incrementQuantity(int quantity) {
        this.quantity = this.quantity + quantity;
    }
    
    public BigDecimal getTotalPrice() {
        return (new BigDecimal(quantity).multiply(product.getPrice(size)));
    }
    
    public String getSkuCodeWithSize() {
        return sKuCodeWithSize;
    }
    
    public void setSkuCodeWithSize(String skuCode) {
        this.sKuCodeWithSize = skuCode;
    }
    
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
    
    public Map<String, Object> getProductDisplayFeatures() {
        return product.getProductDisplayFeatures(size);
    }
    

    public CartItem getParentCartItem() {
        return parentCartItem;
    }

    public void setParentCartItem(CartItem parentCartItem) {
        this.parentCartItem = parentCartItem;
    }

    public Product getParentCartItemProduct() {
        if(parentCartItem != null) {
            return parentCartItem.getProduct();
        }
        return null;
    }

    public Set<CartItem> getChildCartItems() {
        return childCartItems;
    }

    public boolean isActiveCartItem() {
        return product.isProductActive() && isActive();
    }
    
    public void setChildCartItems(HashSet<CartItem> childCartItems) {
        this.childCartItems = childCartItems;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((parentCartItem == null) ? 0 : parentCartItem.hashCode());
        result = prime * result + ((sKuCodeWithSize == null) ? 0 : sKuCodeWithSize.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof CartItem)) {
            return false;
        }
        CartItem other = (CartItem) obj;
        if (parentCartItem == null) {
            if (other.parentCartItem != null) {
                return false;
            }
        } else if (!parentCartItem.equals(other.parentCartItem)) {
            return false;
        }
        if (sKuCodeWithSize == null) {
            if (other.sKuCodeWithSize != null) {
                return false;
            }
        } else if (!sKuCodeWithSize.equals(other.sKuCodeWithSize)) {
            return false;
        }
        return true;
    }
    
    @Override
    public int compareTo(CartItem o) {
        if (isActive() && !o.isActive()) {
            return 1;
        } else if (!isActive() && o.isActive()) {
            return -1;
        }
        return 0;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        // sb.append(super.toString());
        sb.append("CartItem : id=").append(getId());
        sb.append("{\n\t Quantity=").append(quantity).append("\n");
        sb.append("\t Size=").append(size).append("\n");
        sb.append("\t SkuCodeWithSize=").append(sKuCodeWithSize).append("\n");
        sb.append("\t TotalPrice=").append(getTotalPrice());
        sb.append("\n}");
        return sb.toString();
    }
    

    public Object clone() { 
        try {
            CartItem clonedParentCartItem = (CartItem) super.clone();
            clonedParentCartItem.setId(0l);
            return clonedParentCartItem;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public int getChildQuantityCount() {
        int childItemQuantity = 0;
        for (CartItem eachCartItem : getChildCartItems()) {
            childItemQuantity += eachCartItem.getQuantity();
        }
        return childItemQuantity;
    }
    
    /* Get n hierarchy child Item  list*/
    public List<CartItem> getDeepChildItemsList() {
        List<CartItem> allChildCartItems = new ArrayList<CartItem>();
        allChildCartItems.addAll(childCartItems);
        for (CartItem eachChildCartItem : childCartItems) {
            allChildCartItems.addAll(eachChildCartItem.getDeepChildItemsList());
        }
        return allChildCartItems;
    }

}
