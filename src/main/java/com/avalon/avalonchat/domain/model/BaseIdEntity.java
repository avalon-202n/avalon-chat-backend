package com.avalon.avalonchat.domain.model;

import static javax.persistence.GenerationType.*;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

/**
 * BaseIdEntity
 * 모든 id 필드를 가지는 엔티티의 MappedSuperclass
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseIdEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(updatable = false)
	private Long id;
}
