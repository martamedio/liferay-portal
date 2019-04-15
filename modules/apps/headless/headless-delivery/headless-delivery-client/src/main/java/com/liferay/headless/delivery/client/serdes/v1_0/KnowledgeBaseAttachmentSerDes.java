/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.headless.delivery.client.serdes.v1_0;

import com.liferay.headless.delivery.client.dto.v1_0.KnowledgeBaseAttachment;
import com.liferay.headless.delivery.client.json.BaseJSONParser;

import java.util.Objects;

import javax.annotation.Generated;

/**
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
public class KnowledgeBaseAttachmentSerDes {

	public static KnowledgeBaseAttachment toDTO(String json) {
		KnowledgeBaseAttachmentJSONParser knowledgeBaseAttachmentJSONParser =
			new KnowledgeBaseAttachmentJSONParser();

		return knowledgeBaseAttachmentJSONParser.parseToDTO(json);
	}

	public static KnowledgeBaseAttachment[] toDTOs(String json) {
		KnowledgeBaseAttachmentJSONParser knowledgeBaseAttachmentJSONParser =
			new KnowledgeBaseAttachmentJSONParser();

		return knowledgeBaseAttachmentJSONParser.parseToDTOs(json);
	}

	public static String toJSON(
		KnowledgeBaseAttachment knowledgeBaseAttachment) {

		if (knowledgeBaseAttachment == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		sb.append("\"contentUrl\": ");

		if (knowledgeBaseAttachment.getContentUrl() == null) {
			sb.append("null");
		}
		else {
			sb.append("\"");

			sb.append(knowledgeBaseAttachment.getContentUrl());

			sb.append("\"");
		}

		sb.append(", ");

		sb.append("\"encodingFormat\": ");

		if (knowledgeBaseAttachment.getEncodingFormat() == null) {
			sb.append("null");
		}
		else {
			sb.append("\"");

			sb.append(knowledgeBaseAttachment.getEncodingFormat());

			sb.append("\"");
		}

		sb.append(", ");

		sb.append("\"fileExtension\": ");

		if (knowledgeBaseAttachment.getFileExtension() == null) {
			sb.append("null");
		}
		else {
			sb.append("\"");

			sb.append(knowledgeBaseAttachment.getFileExtension());

			sb.append("\"");
		}

		sb.append(", ");

		sb.append("\"id\": ");

		if (knowledgeBaseAttachment.getId() == null) {
			sb.append("null");
		}
		else {
			sb.append(knowledgeBaseAttachment.getId());
		}

		sb.append(", ");

		sb.append("\"sizeInBytes\": ");

		if (knowledgeBaseAttachment.getSizeInBytes() == null) {
			sb.append("null");
		}
		else {
			sb.append(knowledgeBaseAttachment.getSizeInBytes());
		}

		sb.append(", ");

		sb.append("\"title\": ");

		if (knowledgeBaseAttachment.getTitle() == null) {
			sb.append("null");
		}
		else {
			sb.append("\"");

			sb.append(knowledgeBaseAttachment.getTitle());

			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	private static class KnowledgeBaseAttachmentJSONParser
		extends BaseJSONParser<KnowledgeBaseAttachment> {

		protected KnowledgeBaseAttachment createDTO() {
			return new KnowledgeBaseAttachment();
		}

		protected KnowledgeBaseAttachment[] createDTOArray(int size) {
			return new KnowledgeBaseAttachment[size];
		}

		protected void setField(
			KnowledgeBaseAttachment knowledgeBaseAttachment,
			String jsonParserFieldName, Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "contentUrl")) {
				if (jsonParserFieldValue != null) {
					knowledgeBaseAttachment.setContentUrl(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "encodingFormat")) {
				if (jsonParserFieldValue != null) {
					knowledgeBaseAttachment.setEncodingFormat(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "fileExtension")) {
				if (jsonParserFieldValue != null) {
					knowledgeBaseAttachment.setFileExtension(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "id")) {
				if (jsonParserFieldValue != null) {
					knowledgeBaseAttachment.setId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "sizeInBytes")) {
				if (jsonParserFieldValue != null) {
					knowledgeBaseAttachment.setSizeInBytes(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "title")) {
				if (jsonParserFieldValue != null) {
					knowledgeBaseAttachment.setTitle(
						(String)jsonParserFieldValue);
				}
			}
			else {
				throw new IllegalArgumentException(
					"Unsupported field name " + jsonParserFieldName);
			}
		}

	}

}