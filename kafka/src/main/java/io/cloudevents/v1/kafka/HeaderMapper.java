package io.cloudevents.v1.kafka;

import static io.cloudevents.v1.kafka.AttributeMapper.HEADER_PREFIX;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;

import io.cloudevents.extensions.ExtensionFormat;
import io.cloudevents.fun.FormatHeaderMapper;
import io.cloudevents.v1.AttributesImpl;
import io.cloudevents.v1.ContextAttributes;

/**
 * 
 * @author fabiojose
 * @version 1.0
 */
public class HeaderMapper {
	private HeaderMapper() {}
	
	private static final Serializer<String> SERIALIZER = 
			Serdes.String().serializer();
	
	private static final String KAFKA_CONTENT_TYPE = "content-type";

	/**
	 * Following the signature of {@link FormatHeaderMapper}
	 * @param attributes The map of attributes created by 
	 * {@link AttributesImpl#marshal(AttributesImpl)}
	 * @param extensions The map of extensions created by 
	 * {@link ExtensionFormat#marshal(java.util.Collection)}
	 * @return The map of Kafka Headers with values as {@code byte[]}
	 */
	public static Map<String, byte[]> map(Map<String, String> attributes,
			Map<String, String> extensions) {
		Objects.requireNonNull(attributes);
		Objects.requireNonNull(extensions);
		
		Map<String, byte[]> result = attributes.entrySet()
			.stream()
			.filter(attribute -> null!= attribute.getValue())
			.map(attribute -> 
				new SimpleEntry<>(attribute.getKey()
					.toLowerCase(Locale.US), attribute.getValue()))
			.filter(header -> !header.getKey()
					.equals(ContextAttributes.datacontenttype.name()))
			.map(attribute -> 
				new SimpleEntry<>(HEADER_PREFIX+attribute.getKey(),
					attribute.getValue()))
			.map(attribute -> 
				new SimpleEntry<>(attribute.getKey(), 
						SERIALIZER.serialize(null, attribute.getValue())))
			.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
		
		result.putAll(
			extensions.entrySet()
				.stream()
				.filter(extension -> null!= extension.getValue())
				.map(extension -> 
					new SimpleEntry<>(extension.getKey(), 
						SERIALIZER.serialize(null, extension.getValue())))
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue))
		);
		
		Optional.ofNullable(attributes
				.get(ContextAttributes.datacontenttype.name()))
			.ifPresent((dct) -> {
				result.put(KAFKA_CONTENT_TYPE, SERIALIZER.serialize(null, dct));
			});
		
		return result;
	}
}
