package com.bluestone.app.design.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.envers.Audited;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;

import com.bluestone.app.core.model.BaseEntity;
import com.bluestone.app.search.tag.model.Tag;

@Audited
@Entity
@Table(name = "design")
public class Design extends BaseEntity {
    
    private static final long              serialVersionUID         = 1L;
    
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    @Column(name = "design_code", unique = true, nullable = false)
    private String                         designCode;
    
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    @Column(name = "long_desc", nullable = false, length = 512)
    private String                         longDescription;
    
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    @Column(name = "design_name", nullable = false)
    private String                         designName;
    
    private float                          height;
    
    private float                          width;
    
    private boolean                        isPdiSheetavailable;
    
    @Column(name = "is_customization_supported", columnDefinition = "tinyint(1)", nullable = false)
    private short                          isCustomizationSupported = 0;
    
    @Transient
    private List<String>                   error                    = new LinkedList<String>();
    
    @IndexedEmbedded
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "design_category_id", nullable = false)
    private DesignCategory                 designCategory;
    
    @IndexedEmbedded
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "design_tag", joinColumns = { @JoinColumn(name = "design_id") }, inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @BatchSize(size=30)
    private Set<Tag>                       tags;
    
    @OrderBy("priority ASC")
    @OneToMany(mappedBy = "design", cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    private List<Customization>            customizations;
    
    @OneToMany(mappedBy = "design", cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    @BatchSize(size=10)
    private List<DesignMetalFamily>        designMetalFamilies;
    
    @OneToOne(mappedBy = "design", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private DesignImageView                designImageView;
    
    @LazyCollection(LazyCollectionOption.TRUE)
    @OneToMany(mappedBy = "design", cascade = CascadeType.ALL)
    @BatchSize(size=10)
    private List<DesignStoneSpecification> designStoneSpecifications;
    
    @Transient
    private short                          edit                     = 0;
    
    @Column(name = "percentage_adjustment", nullable = true, columnDefinition = "double DEFAULT '0'")
    private double                         percentage_adjustment = 0.0;
    
    public DesignImageView getDesignImageView() {
        return designImageView;
    }
    
    public void setDesignImageView(DesignImageView designImageView) {
        this.designImageView = designImageView;
    }
    
    public List<Customization> getCustomizations() {
        return customizations;
    }
    
    public void setCustomizations(List<Customization> customizations) {
        this.customizations = customizations;
    }
    
    public float getHeight() {
        return height;
    }
    
    public void setHeight(float height) {
        this.height = height;
    }
    
    public float getWidth() {
        return width;
    }
    
    public void setWidth(float width) {
        this.width = width;
    }
    
    public DesignCategory getDesignCategory() {
        return designCategory;
    }
    
    public void setDesignCategory(DesignCategory designCategory) {
        this.designCategory = designCategory;
    }
    
    public Set<Tag> getTags() {
        return tags;
    }
    
    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }
    
    public String getDesignName() {
        return designName;
    }
    
    public void setDesignName(String designName) {
        this.designName = designName;
    }
    
    public String getDesignCode() {
        return designCode;
    }
    
    public void setDesignCode(String designCode) {
        this.designCode = designCode;
    }
    
    public String getLongDescription() {
        return longDescription;
    }
    
    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }
    
    public List<String> getError() {
        return error;
    }
    
    public void setError(List<String> error) {
        this.error = error;
    }
    
    public boolean isPdiSheetavailable() {
        return isPdiSheetavailable;
    }
    
    public void setPdiSheetavailable(boolean isPdiSheetavailable) {
        this.isPdiSheetavailable = isPdiSheetavailable;
    }
    
    public List<DesignStoneSpecification> getDesignStoneSpecifications() {
       return this.designStoneSpecifications;
        
    }
    
    public void setDesignStoneSpecifications(List<DesignStoneSpecification> designStoneSpecifications) {
        this.designStoneSpecifications = designStoneSpecifications;
    }
    
    public List<DesignMetalFamily> getDesignMetalFamilies() {
        return designMetalFamilies;
    }
    
    public void setDesignMetalFamilies(List<DesignMetalFamily> designMetalFamilies) {
        this.designMetalFamilies = designMetalFamilies;
    }
    
    @Override
    public String toString() {
        return "Design [designCode=" + designCode + ", longDescription=" + longDescription + ", designName="
                + designName + ", designCategory=" + designCategory + ", tags=" + tags + "]";
    }

    public String toShortString() {
        return "Design [designCode=" + designCode + ", longDescription=" + longDescription + ", designName="
               + designName + ", designCategory=" + designCategory + "]";
    }
    
    public short getIsCustomizationSupported() {
        return isCustomizationSupported;
    }
    
    public void setIsCustomizationSupported(short isCustomizationSupported) {
        this.isCustomizationSupported = isCustomizationSupported;
    }
    
    public short getEdit() {
        return edit;
    }
    
    public void setEdit(short edit) {
        this.edit = edit;
    }
    
}
