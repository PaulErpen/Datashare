#!/bin/sh -e

###
# Copyright (c) 2015-2018, Antoine Le Gonidec
# All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are met:
#
# 1. Redistributions of source code must retain the above copyright notice,
# this list of conditions and the following disclaimer.
#
# 2. Redistributions in binary form must reproduce the above copyright notice,
# this list of conditions and the following disclaimer in the documentation
# and/or other materials provided with the distribution.
#
# This software is provided by the copyright holders and contributors "as is"
# and any express or implied warranties, including, but not limited to, the
# implied warranties of merchantability and fitness for a particular purpose
# are disclaimed. In no event shall the copyright holder or contributors be
# liable for any direct, indirect, incidental, special, exemplary, or
# consequential damages (including, but not limited to, procurement of
# substitute goods or services; loss of use, data, or profits; or business
# interruption) however caused and on any theory of liability, whether in
# contract, strict liability, or tort (including negligence or otherwise)
# arising in any way out of the use of this software, even if advised of the
# possibility of such damage.
###

###
# conversion script for the Star Wars: Knights of the Old Republic installer sold on GOG.com
# build a .deb package from the Windows installer
# tested on Debian, should work on any .deb-based distribution
#
# script version 20151127.1
#
# send your bug reports to vv221@dotslashplay.it
# start the e-mail subject by "./play.it" to avoid it being flagged as spam
###

# Set game-specific variables

GAME_ID='star-wars-kotor'
GAME_ID_SHORT='kotor1'
GAME_NAME='Knights of the Old Republic'
GAME_NAME_LONG='Star Wars: Knights of the Old Republic'
GOG_ID='1207666283'

GAME_ARCHIVE1='setup_sw_kotor_2.0.0.3.bin'
GAME_ARCHIVE1_MD5='2c2cc27ee410948b417f8fa30d0c9201'
GAME_ARCHIVE2='setup_sw_kotor_french_2.0.0.3.bin'
GAME_ARCHIVE2_MD5='81c2b828d0b2708fae8c7b15248bb22d'
GAME_ARCHIVE_FULLSIZE='3800000'
PKG_ORIGIN='gog'
PKG_REVISION='2.0.0.3'

GAME_CACHE_DIRS=''
GAME_CACHE_FILES=''
GAME_CACHE_FILES_POST=''
GAME_CONFIG_DIRS=''
GAME_CONFIG_FILES='./swkotor.ini'
GAME_CONFIG_FILES_POST=''
GAME_DATA_DIRS='./override ./saves'
GAME_DATA_FILES=''
GAME_DATA_FILES_POST=''

APP_COMMON_ID="${GAME_ID_SHORT}-common.sh"

APP1_ID="${GAME_ID}"
APP1_EXE='./swkotor.exe'
APP1_ICON='./swkotor.exe'
APP1_ICON_RES='32x32 48x48'
APP1_NAME="${GAME_NAME}"
APP1_NAME_FR="${GAME_NAME}"
APP1_CAT='Game'

APP2_ID="${GAME_ID}_config"
APP2_EXE='./swconfig.exe'
APP2_ICON='./swconfig.exe'
APP2_ICON_RES='32x32 48x48'
APP2_NAME="${GAME_NAME} (settings)"
APP2_NAME_FR="${GAME_NAME} (réglages)"
APP2_CAT='Settings'

PKG1_ID="${GAME_ID}"
PKG1_VERSION='1.03'
PKG1_ARCH='i386'
PKG1_CONFLICTS=''
PKG1_DEPS='wine:amd64 | wine, wine32 | wine-bin | wine1.6-i386 | wine1.4-i386 | wine-staging-i386'
PKG1_RECS=''
PKG1_DESC="${GAME_NAME_LONG}"

# Load common functions

TARGET_LIB_VERSION='1.13'
if [ -z "${PLAYIT_LIB}" ]; then
	PLAYIT_LIB='./play-anything.sh'
fi
if ! [ -e "${PLAYIT_LIB}" ]; then
	printf '\n\033[1;31mError:\033[0m\n'
	printf 'play-anything.sh not found.\n'
	printf 'It must be placed in the same directory than this script.\n\n'
	exit 1
fi
LIB_VERSION="$(grep '^# library version' "${PLAYIT_LIB}" | cut -d' ' -f4 | cut -d'.' -f1,2)"
if [ ${LIB_VERSION%.*} -ne ${TARGET_LIB_VERSION%.*} ] || [ ${LIB_VERSION#*.} -lt ${TARGET_LIB_VERSION#*.} ]; then
	printf '\n\033[1;31mError:\033[0m\n'
	printf 'Wrong version of play-anything.\n'
	printf 'It must be at least %s ' "${TARGET_LIB_VERSION}"
	printf 'but lower than %s.\n\n' "$((${TARGET_LIB_VERSION%.*}+1)).0"
	exit 1
fi
. "${PLAYIT_LIB}"

# Set extra variables

PKG_PREFIX_DEFAULT='/usr/local'
PKG_COMPRESSION_DEFAULT='none'
GAME_ARCHIVE_CHECKSUM_DEFAULT='md5sum'
GAME_LANG_DEFAULT=''
WITH_MOVIES_DEFAULT=''

printf '\n'
game_mkdir 'PKG_TMPDIR' "$(mktemp -u ${GAME_ID_SHORT}.XXXXX)" "$((${GAME_ARCHIVE_FULLSIZE}*2))"
game_mkdir 'PKG1_DIR' "${PKG1_ID}_${PKG1_VERSION}-${PKG_ORIGIN}${PKG_REVISION}_${PKG1_ARCH}" "$((${GAME_ARCHIVE_FULLSIZE}*2))"
fetch_args "$@"
check_deps 'fakeroot unar' 'icotool wrestool'
printf '\n'
set_checksum
set_compression
set_prefix

PATH_BIN="${PKG_PREFIX}/games"
PATH_DESK='/usr/local/share/applications'
PATH_DOC="${PKG_PREFIX}/share/doc/${GAME_ID}"
PATH_GAME="${PKG_PREFIX}/share/games/${GAME_ID}"
PATH_ICON_BASE='/usr/local/share/icons/hicolor'

printf '\n'
set_target '2' 'gog.com'
printf '\n'

# Check target file integrity

if [ "${GAME_ARCHIVE_CHECKSUM}" = 'md5sum' ]; then
	checksum "${GAME_ARCHIVE}" 'defaults' "${GAME_ARCHIVE1_MD5}" "${GAME_ARCHIVE2_MD5}"
fi

# Extract game data

ARCHIVE_PASSWD="$(printf '%s' "${GOG_ID}" | md5sum | cut -d' ' -f1)"
build_pkg_dirs '1' "${PATH_BIN}" "${PATH_DESK}" "${PATH_DOC}" "${PATH_GAME}"
print wait
extract_data 'unar_passwd' "${GAME_ARCHIVE}" "${PKG_TMPDIR}" 'quiet,tolower' "${ARCHIVE_PASSWD}"
for file in 'manual.pdf' 'swkotorv103.txt'; do
	mv "${PKG_TMPDIR}/game"/${file} "${PKG1_DIR}${PATH_DOC}"
done
if [ -d "${PKG_TMPDIR}/game/docs" ]; then
	mv "${PKG_TMPDIR}/game/docs"/* "${PKG1_DIR}${PATH_DOC}"
	rmdir "${PKG_TMPDIR}/game/docs"
fi
mv "${PKG_TMPDIR}/game"/* "${PKG1_DIR}${PATH_GAME}"
mv "${PKG_TMPDIR}/support/app/swkotor.ini" "${PKG1_DIR}${PATH_GAME}"
if [ "${NO_ICON}" = '0' ]; then
	extract_icons "${APP1_ID}" "${APP1_ICON}" "${APP1_ICON_RES}" "${PKG_TMPDIR}"
	extract_icons "${APP2_ID}" "${APP2_ICON}" "${APP2_ICON_RES}" "${PKG_TMPDIR}"
fi
rm -rf "${PKG_TMPDIR}"
print done

# Write launchers

write_bin_wine_common "${PKG1_DIR}${PATH_BIN}/${APP_COMMON_ID}"
write_bin_wine_cfg "${PKG1_DIR}${PATH_BIN}/${GAME_ID_SHORT}-winecfg"
write_bin_wine "${PKG1_DIR}${PATH_BIN}/${APP1_ID}" "${APP1_EXE}" '' '' "${APP1_NAME}"
write_bin_wine "${PKG1_DIR}${PATH_BIN}/${APP2_ID}" "${APP2_EXE}" '' '' "${APP2_NAME}"
write_desktop "${APP1_ID}" "${APP1_NAME}" "${APP1_NAME_FR}" "${PKG1_DIR}${PATH_DESK}/${APP1_ID}.desktop" "${APP1_CAT}" 'wine'
write_desktop "${APP2_ID}" "${APP2_NAME}" "${APP2_NAME_FR}" "${PKG1_DIR}${PATH_DESK}/${APP2_ID}.desktop" "${APP2_CAT}" 'wine'
printf '\n'

# Build package

write_pkg_debian "${PKG1_DIR}" "${PKG1_ID}" "${PKG1_VERSION}-${PKG_ORIGIN}${PKG_REVISION}" "${PKG1_ARCH}" "${PKG1_CONFLICTS}" "${PKG1_DEPS}" "${PKG1_RECS}" "${PKG1_DESC}"
build_pkg "${PKG1_DIR}" "${PKG1_DESC}" "${PKG_COMPRESSION}" 'defaults'
print_instructions "${PKG1_DESC}" "${PKG1_DIR}"
printf '\n%s ;)\n\n' "$(l10n 'have_fun')"

exit 0
