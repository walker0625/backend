package kr.lifesemantics.canofymd.modulecore.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import kr.lifesemantics.canofymd.modulecore.constants.KeyConstants;
import kr.lifesemantics.canofymd.modulecore.enums.AuthType;
import kr.lifesemantics.canofymd.modulecore.enums.Category;
import kr.lifesemantics.canofymd.modulecore.enums.UserType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtUtil {

    private final Key key;

    // TODO key 값 간소하게 설정하는 것으로 수정(알고리즘 변경)
    public JwtUtil() {
        byte[] keyBytes = Decoders.BASE64.decode(KeyConstants.JWT_SECRET);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createJwt(AuthType authType, UserType userType, String id, String name, Long seq, Long hospitalSeq, Long activeMinute, List<Category> category) {

        Claims claims = Jwts.claims();
        claims.put("authType", authType);
        claims.put("userType", userType);
        claims.put("id", id);
        claims.put("name", name);
        claims.put("seq", seq);
        claims.put("hospitalSeq", hospitalSeq);
        claims.put("category", category.stream().map(i -> i.name()).collect(Collectors.joining(",")));

        ZonedDateTime issueTime = ZonedDateTime.now();

        // activeMinute가 null이면 만료되지 않는 토큰(expiredDate가 null)
        Date expiredDate = activeMinute == null ? null :
                Date.from(issueTime.plusMinutes(activeMinute).toInstant());

        return Jwts.builder()
                        .setClaims(claims)
                        .setIssuedAt(Date.from(issueTime.toInstant()))
                        .setExpiration(expiredDate)
                        .signWith(key, SignatureAlgorithm.HS256)
                    .compact();
    }

    public String getAuthType(String jwt) {
        return parseClaims(jwt).get("authType", String.class);
    }

    public String getId(String jwt) {
        return parseClaims(jwt).get("id", String.class);
    }

    public Long getSeq(String jwt) {
        return parseClaims(jwt).get("seq", Long.class);
    }

    public List<Category> getCategory(String jwt) {

        String category = parseClaims(jwt).get("category", String.class);
        List<Category> categories = List.of(category.split(",")).stream().map(i -> Category.valueOf(i)).toList();
        return categories;
    }

    // TODO 던질 예외 추후 추가
    public boolean validateToken(String jwt) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }

        return false;
    }

    public Claims parseClaims(String jwt) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

}