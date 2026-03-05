import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    stages: [
        { duration: '10s', target: 500 },  // Ramp up to 500 users
        { duration: '30s', target: 2000 }, // Spike to 2000 users
        { duration: '10s', target: 5000 }, // BlitzScale to 5000 users
        { duration: '10s', target: 0 },    // Ramp down gracefully
    ],
};

export function setup() {
    const authUrl = 'http://localhost:8081/realms/blitzscale-realm/protocol/openid-connect/token';
    const payload = {
        client_id: 'blitzscale-client',
        grant_type: 'password',
        username: 'testuser',
        password: 'password',
    };

    const res = http.post(authUrl, payload);

    return { token: res.json('access_token') };
}

export default function (data) {
    const url = 'http://localhost:8080/api/v1/flash-sales/12/buy';

    const payload = JSON.stringify({
        productId: 8,
        quantity: 1,
        userId: `load-tester-${__VU}`
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${data.token}`,
        },
    };

    const res = http.post(url, payload, params);

    check(res, {
        'Accepted (202)': (r) => r.status === 202,
        'Rate Limited (429)': (r) => r.status === 429,
        'Server Error (500)': (r) => r.status === 500,
    });

    sleep(1);
}