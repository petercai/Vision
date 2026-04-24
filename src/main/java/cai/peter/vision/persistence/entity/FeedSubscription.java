package cai.peter.vision.persistence.entity;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "FEEDSUBSCRIPTIONS")
@SuppressWarnings("serial")
@Getter
@Setter
public class FeedSubscription extends AbstractModel {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Feed feed;

    @Override
    public String toString() {
        return "FeedSubscription{" +
                "title='" + title + '\'' +
                ", filter='" + filter + '\'' +
                '}';
    }

    @Column(length = 128, nullable = false)
	private String title;

	@ManyToOne(fetch = FetchType.LAZY)
	private FeedCategory category;

	@OneToMany(mappedBy = "subscription", cascade = CascadeType.REMOVE)
	private Set<FeedEntryStatus> statuses;

	private Integer position;

	@Column(length = 4096)
	private String filter;

}

