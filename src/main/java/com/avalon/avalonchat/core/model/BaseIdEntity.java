package com.avalon.avalonchat.core.model;

import static javax.persistence.GenerationType.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * BaseIdEntity
 * 모든 id 필드를 가지는 엔티티의 MappedSuperclass
 */
@Getter
@EqualsAndHashCode
@MappedSuperclass
public abstract class BaseIdEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(updatable = false)
	private Long id;
}
