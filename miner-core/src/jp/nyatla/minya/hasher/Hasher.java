/********************************************************************************
 * MiNya pjeject
 * Copyright 2014 nyatla.jp
 * https://github.com/nyatla/JMiNya
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 ********************************************************************************/
/********************************************************************************
 * Original source code by pooler
 * https://github.com/pooler/JMiner
 *
 * Copyright 2011 LitecoinPool.org
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License, version 2, as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 ********************************************************************************/

package jp.nyatla.minya.hasher;

import static java.lang.Integer.rotateLeft;
import static java.lang.System.arraycopy;

import java.security.GeneralSecurityException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Hasher {

	private final Mac mac;
	private final byte[] H = new byte[32];
	private final byte[] B = new byte[128 + 4];
	private final int[] X = new int[32];
	private final int[] V = new int[32 * 1024];

	public Hasher () throws GeneralSecurityException {
		this.mac = Mac.getInstance("HmacSHA256");
	}

	public byte[] hash (final byte[] header, final int nonce) throws GeneralSecurityException {
		int i, j, k;

		arraycopy(header, 0, this.B, 0, 76);
		this.B[76] = (byte)(nonce >> 24);
		this.B[77] = (byte)(nonce >> 16);
		this.B[78] = (byte)(nonce >> 8);
		this.B[79] = (byte)(nonce >> 0);
		this.mac.init(new SecretKeySpec(this.B, 0, 80, "HmacSHA256"));
		this.B[80] = 0;
		this.B[81] = 0;
		this.B[82] = 0;
		for (i = 0; i < 4; i++) {
			this.B[83] = (byte)(i + 1);
			this.mac.update(this.B, 0, 84);
			this.mac.doFinal(this.H, 0);

			for (j = 0; j < 8; j++) {
				this.X[i * 8 + j] = (this.H[j * 4 + 0] & 0xff) << 0 | (this.H[j * 4 + 1] & 0xff) << 8
					| (this.H[j * 4 + 2] & 0xff) << 16 | (this.H[j * 4 + 3] & 0xff) << 24;
			}
		}

		for (i = 0; i < 1024; i++) {
			arraycopy(this.X, 0, this.V, i * 32, 32);
			this.xorSalsa8(0, 16);
			this.xorSalsa8(16, 0);
		}
		for (i = 0; i < 1024; i++) {
			k = (this.X[16] & 1023) * 32;
			for (j = 0; j < 32; j++) {
				this.X[j] ^= this.V[k + j];
			}
			this.xorSalsa8(0, 16);
			this.xorSalsa8(16, 0);
		}

		for (i = 0; i < 32; i++) {
			this.B[i * 4 + 0] = (byte)(this.X[i] >> 0);
			this.B[i * 4 + 1] = (byte)(this.X[i] >> 8);
			this.B[i * 4 + 2] = (byte)(this.X[i] >> 16);
			this.B[i * 4 + 3] = (byte)(this.X[i] >> 24);
		}

		this.B[128 + 3] = 1;
		this.mac.update(this.B, 0, 128 + 4);
		this.mac.doFinal(this.H, 0);

		return this.H;
	}

	private void xorSalsa8 (final int di, final int xi) {
		int x00 = (this.X[di + 0] ^= this.X[xi + 0]);
		int x01 = (this.X[di + 1] ^= this.X[xi + 1]);
		int x02 = (this.X[di + 2] ^= this.X[xi + 2]);
		int x03 = (this.X[di + 3] ^= this.X[xi + 3]);
		int x04 = (this.X[di + 4] ^= this.X[xi + 4]);
		int x05 = (this.X[di + 5] ^= this.X[xi + 5]);
		int x06 = (this.X[di + 6] ^= this.X[xi + 6]);
		int x07 = (this.X[di + 7] ^= this.X[xi + 7]);
		int x08 = (this.X[di + 8] ^= this.X[xi + 8]);
		int x09 = (this.X[di + 9] ^= this.X[xi + 9]);
		int x10 = (this.X[di + 10] ^= this.X[xi + 10]);
		int x11 = (this.X[di + 11] ^= this.X[xi + 11]);
		int x12 = (this.X[di + 12] ^= this.X[xi + 12]);
		int x13 = (this.X[di + 13] ^= this.X[xi + 13]);
		int x14 = (this.X[di + 14] ^= this.X[xi + 14]);
		int x15 = (this.X[di + 15] ^= this.X[xi + 15]);
		for (int i = 0; i < 8; i += 2) {
			x04 ^= rotateLeft(x00 + x12, 7);
			x08 ^= rotateLeft(x04 + x00, 9);
			x12 ^= rotateLeft(x08 + x04, 13);
			x00 ^= rotateLeft(x12 + x08, 18);
			x09 ^= rotateLeft(x05 + x01, 7);
			x13 ^= rotateLeft(x09 + x05, 9);
			x01 ^= rotateLeft(x13 + x09, 13);
			x05 ^= rotateLeft(x01 + x13, 18);
			x14 ^= rotateLeft(x10 + x06, 7);
			x02 ^= rotateLeft(x14 + x10, 9);
			x06 ^= rotateLeft(x02 + x14, 13);
			x10 ^= rotateLeft(x06 + x02, 18);
			x03 ^= rotateLeft(x15 + x11, 7);
			x07 ^= rotateLeft(x03 + x15, 9);
			x11 ^= rotateLeft(x07 + x03, 13);
			x15 ^= rotateLeft(x11 + x07, 18);
			x01 ^= rotateLeft(x00 + x03, 7);
			x02 ^= rotateLeft(x01 + x00, 9);
			x03 ^= rotateLeft(x02 + x01, 13);
			x00 ^= rotateLeft(x03 + x02, 18);
			x06 ^= rotateLeft(x05 + x04, 7);
			x07 ^= rotateLeft(x06 + x05, 9);
			x04 ^= rotateLeft(x07 + x06, 13);
			x05 ^= rotateLeft(x04 + x07, 18);
			x11 ^= rotateLeft(x10 + x09, 7);
			x08 ^= rotateLeft(x11 + x10, 9);
			x09 ^= rotateLeft(x08 + x11, 13);
			x10 ^= rotateLeft(x09 + x08, 18);
			x12 ^= rotateLeft(x15 + x14, 7);
			x13 ^= rotateLeft(x12 + x15, 9);
			x14 ^= rotateLeft(x13 + x12, 13);
			x15 ^= rotateLeft(x14 + x13, 18);
		}
		this.X[di + 0] += x00;
		this.X[di + 1] += x01;
		this.X[di + 2] += x02;
		this.X[di + 3] += x03;
		this.X[di + 4] += x04;
		this.X[di + 5] += x05;
		this.X[di + 6] += x06;
		this.X[di + 7] += x07;
		this.X[di + 8] += x08;
		this.X[di + 9] += x09;
		this.X[di + 10] += x10;
		this.X[di + 11] += x11;
		this.X[di + 12] += x12;
		this.X[di + 13] += x13;
		this.X[di + 14] += x14;
		this.X[di + 15] += x15;
	}
}
