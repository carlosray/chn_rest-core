package ru.vas.restcore.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtUtils {
    @Value("jwt.secret")
    private String jwtSecret;
    private final JwtClaimsConfig jwtClaimsConfig;
    private final static String HEADER_PREFIX = "Bearer ";

    public Optional<Claims> getClaims(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return Optional.ofNullable(claimsJws.getBody());
        } catch (ExpiredJwtException expEx) {
            log.error("Token expired", expEx);
        } catch (UnsupportedJwtException unsEx) {
            log.error("Unsupported jwt", unsEx);
        } catch (MalformedJwtException mjEx) {
            log.error("Malformed jwt", mjEx);
        } catch (SignatureException sEx) {
            log.error("Invalid signature", sEx);
        } catch (Exception e) {
            log.error("invalid token", e);
        }
        return Optional.empty();
    }

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .claim(jwtClaimsConfig.getLoginKey(), userDetails.getUsername())
                .claim(jwtClaimsConfig.getAuthoritiesKey(), userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(",")))
        .setExpiration(expiration())
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();
    }

    private Date expiration() {
        return Date.from(LocalDateTime.now()
                .plus(10,ChronoUnit.MINUTES).atZone(ZoneId.systemDefault()).toInstant());
    }

    public Optional<String> getToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(StringUtils::hasText)
                .filter(header -> header.startsWith(HEADER_PREFIX))
                .map(header -> header.substring(HEADER_PREFIX.length()));
    }


}
