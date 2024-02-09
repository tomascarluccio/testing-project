#!/usr/bin/env python
import os
from setuptools import setup, find_packages

ROOT_DIR = os.path.dirname(__file__)
SOURCE_DIR = os.path.join(ROOT_DIR)


def get_requirements(requirements_file):
    with open(requirements_file) as f:
        required = [line.split('#')[0] for line in f.read().splitlines()]
    return required


setup(
    name='sms',
    version='1',
    description="SMS",
    author="Altipeak SA",
    author_email="sales@altipeak.com",
    packages=find_packages(),
    zip_safe=False,
    classifiers=[
        'Development Status :: 1 - Production/Stable',
        'Environment :: Web Environment',
        'Framework :: Flask',
        'Intended Audience :: Developers',
        'Operating System :: Alpine 3 / Debian 11 / Debian 12',
        'Programming Language :: Python',
        'Topic :: Web'
    ],
    install_requires=get_requirements(os.path.join(ROOT_DIR, 'requirements.txt')),
    include_package_data=True,
)
