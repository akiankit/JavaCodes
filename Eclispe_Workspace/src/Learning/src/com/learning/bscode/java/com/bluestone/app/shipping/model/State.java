package com.bluestone.app.shipping.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.bluestone.app.core.model.BaseEntity;

@Audited
@Entity
@NamedQueries({
@NamedQuery(name="state.getStatesListQuery",query="select s from State s"),
@NamedQuery(name="state.getStateFromName",query="select s from State s where s.stateName = :stateName")
})
        
@Table(name="state")
public class State extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Column(name="state_name",columnDefinition="VARCHAR(32)",nullable=false)
	private String stateName;

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((stateName == null) ? 0 : stateName.hashCode());
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
        if (!(obj instanceof State)) {
            return false;
        }
        State other = (State) obj;
        if (stateName == null) {
            if (other.stateName != null) {
                return false;
            }
        } else if (!stateName.equals(other.stateName)) {
            return false;
        }
        return true;
    }
	
}
